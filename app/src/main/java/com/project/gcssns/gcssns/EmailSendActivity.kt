package com.project.gcssns.gcssns

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.gcssns.gcssns.adapter.EmailSendAdapter
import com.project.gcssns.gcssns.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_email_send.*
import kotlinx.android.synthetic.main.activity_email_send.view.*
import kotlinx.android.synthetic.main.email_send_row.view.*
import kotlinx.android.synthetic.main.useritem_userlist_row.view.*


class EmailSendActivity : AppCompatActivity() {

    var emailList = ArrayList<String>()
    var emailAdapter : ArrayAdapter<String>? = null
    var adapter = EmailSendAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_send)

        fetchUserEmail()


        emailAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, emailList)
        spinner_email_send_email_list.adapter = emailAdapter
        spinner_email_send_email_list.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                var username = parent.getItemAtPosition(position) as String
                username = username.split("/").get(0)
                var isSelected = false
                for(list in adapter.m_emailArray){
                    if(list.equals(username)){
                        isSelected = true
                        Toast.makeText(applicationContext, "이미 선택된 이메일 입니다.", Toast.LENGTH_LONG).show()
                        return
                    }
                }
                if(!isSelected){
                    adapter.addItem(username)
                    textView_email_send_receiver_num.text = "수신자 : " + adapter.m_emailArray.size + "명"
                }
            }
        }

        recyclerview_email_send_email_lists.adapter = adapter

        button_email_send_submit.setOnClickListener {
            openEmailApp()
        }
    }

    private fun fetchUserEmail(){ //데이터 베이스에서 유저들을 가져와 recycler로 보여준다
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                p0!!.children.forEach{
                    val user = it.getValue(User::class.java)
                    emailList.add(user!!.userEmail!!+"/"+user.userName)
                    emailAdapter!!.notifyDataSetChanged()
                }
            }
            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

    fun openEmailApp(){
        val email = Intent(Intent.ACTION_SEND)
        email.type = "plain/text"
        // email setting 배열로 해놔서 복수 발송 가능
        var address = Array<String>(adapter.m_emailArray.size, {i -> adapter.m_emailArray.get(i)})
        Toast.makeText(this, address.toString(), Toast.LENGTH_LONG).show()
        email.putExtra(Intent.EXTRA_EMAIL, address)
        email.putExtra(Intent.EXTRA_SUBJECT, "제목")
        email.putExtra(Intent.EXTRA_TEXT, editText_email_send_content.text.toString())
        startActivity(email)
    }


    /*class EmailItem(var adapter : GroupAdapter<ViewHolder>, var selectUserList : ArrayList<String>, val email: String) : Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.itemView.textView_email_send_row_email.text = email
            viewHolder.itemView.textView_email_send_row_delete.setOnClickListener {
                adapter.remove(adapter.getItem(position));
                selectUserList.remove(email);
                adapter.notifyDataSetChanged()
            }
        }
        override fun getLayout(): Int {
            return R.layout.email_send_row
        }
    }*/

}
