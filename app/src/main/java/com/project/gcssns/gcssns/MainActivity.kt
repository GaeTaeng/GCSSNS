package com.project.gcssns.gcssns

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.gcssns.gcssns.fragment.ChatFragment
import com.project.gcssns.gcssns.fragment.GalleryFragment
import com.project.gcssns.gcssns.fragment.HomeFragment
import com.project.gcssns.gcssns.fragment.MembersFragment
import com.project.gcssns.gcssns.model.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.toolbar_main.*



class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var currentUser: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){ //로그인 안된 사용자는 main에 올 수 없으므로
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else{
            fetchCurrentUser()
        }
        var homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.main_content, homeFragment).commit()
        //Drawer_Menu
        nav_view.setNavigationItemSelectedListener(this)
    }


    private fun fetchCurrentUser(){ //현재 접속한 유저정보를 데이터베이스에서 찾는다
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                currentUser = p0!!.getValue(User::class.java)
                val targetUserImage = nav_view.getHeaderView(0).nav_header_user_image
                Picasso.get().load(currentUser!!.profileImageUrl).into(targetUserImage)
                nav_view.getHeaderView(0).nav_header_username.text = currentUser!!.userName
                nav_view.getHeaderView(0).nav_header_user_major.text = currentUser!!.userMajor
            }
            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //Bottom menu event
            R.id.action_home->{
                var homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, homeFragment).commit()
                return true
            }
            R.id.action_chat->{
                var chatFragment = ChatFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, chatFragment).commit()
                return true
            }
            R.id.action_gallery->{
                var galleryFragment = GalleryFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, galleryFragment).commit()
                return true
            }
            R.id.action_members->{
                var membersFragment = MembersFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, membersFragment).commit()
                return true
            }

            //side menu event
            R.id.nav_logout-> {
                var builder = AlertDialog.Builder(this)
                builder.setMessage("정말 로그아웃 하시겠습니까?")
                        .setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                        .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which -> logout() })
                builder.show()
            }
            R.id.nav_profile->{
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_email->{
                startActivity(Intent(this, EmailSendActivity::class.java))
            }
        }
        return false
    }

    override fun onBackPressed() {
        var builder = AlertDialog.Builder(this)
        builder.setMessage("앱을 종료 하시겠습니까?")
                .setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which -> appFinish() })
        builder.show()
    }

    fun appFinish(){
        super.onBackPressed()
    }

    fun logout(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == HomeFragment.HOMEFRAGMENT_REQUEST_CODE_HOMEFEED_UP_POSITION && resultCode == Activity.RESULT_OK){
            val position = data!!.getIntExtra("adapterSize", 0)
            Toast.makeText(this, position.toString(), Toast.LENGTH_LONG).show()
            recyclerview_home_list.scrollToPosition(position)
        }
    }

}
