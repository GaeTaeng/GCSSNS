package com.project.gcssns.gcssns

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(main_toolbar)
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null){ //로그인 안된 사용자는 main에 올 수 없으므로
            val intent = Intent(this, SignupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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
            R.id.action_profile->{
                var profileFragment = ProfileFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, profileFragment).commit()
                return true
            }
            R.id.action_members->{
                var membersFragment = MembersFragment()
                supportFragmentManager.beginTransaction().replace(R.id.main_content, membersFragment).commit()
                return true
            }
        }
        return false
    }

}
