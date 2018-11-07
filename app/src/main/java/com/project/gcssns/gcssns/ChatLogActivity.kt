package com.project.gcssns.gcssns

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.project.gcssns.gcssns.model.ChatMessage
import com.project.gcssns.gcssns.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_log_message_from_row.view.*
import kotlinx.android.synthetic.main.chat_log_message_to_row.view.*
import kotlinx.android.synthetic.main.chat_log_toolbar.*
import kotlinx.android.synthetic.main.chat_log_toolbar.view.*

class ChatLogActivity : AppCompatActivity(), View.OnKeyListener {

    var view : View? = null

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if(event!!.action != KeyEvent.ACTION_DOWN){
            return true
        }
        if(keyCode == KeyEvent.KEYCODE_ENTER){
            sendMessage()
        }
        return true
    }

    companion object {
        val TAG = "ChatLogActivity"
    }

    val adapter = GroupAdapter<ViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        editText_chat_log_message.setOnKeyListener(this)

        setSupportActionBar(chat_log_toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        recyclerview_chat_log.adapter = adapter

        toUser = intent.getParcelableExtra<User>(MembersFragment.USER_KEY)
        chat_log_toolbar.textView_chat_log_to_username.text = toUser!!.userName

        listenForMessages()
        imageView_chat_log_back.setOnClickListener {
            this.finish()
        }
        button_chat_log_send.setOnClickListener {
            sendMessage()
        }
    }

    private fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        if(fromId == null) return
        val toId = toUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object: ChildEventListener{ //데이터 베이스에서 메세지 정보를 찾아서 뷰에 표시해 준다
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val chatMessage = p0!!.getValue(ChatMessage::class.java)
                if(chatMessage != null){
                    Log.d(TAG, chatMessage.text)
                    if(chatMessage.toId == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatFromItem(chatMessage.text, toUser!!))
                    } else if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
                        val currentUser = MainActivity.currentUser ?: return
                        adapter.add(ChatToItem(chatMessage.text, currentUser!!))
                    }
                    recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot?) {
            }

        })
    }

    private fun sendMessage(){
        val text = editText_chat_log_message.text.toString()
        if(text.isEmpty()) return
        val fromId = FirebaseAuth.getInstance().uid
        if(fromId == null) return

        val toId = toUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(ref.key, text, fromId, toId, System.currentTimeMillis() / 1000)
        ref.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "saved our message : ${ref.key}")
                    editText_chat_log_message.text.clear()
                    recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
                }
        toRef.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG, "saved our message : ${toRef.key}")
                }
    }

}

class ChatFromItem(val text: String?, val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_log_from_row.text = text
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageView_chat_log_from_row
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_log_message_from_row
    }
}

class ChatToItem(val text: String? , val user: User): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_log_to_row.text = text
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageView_chat_log_to_row
        Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_log_message_to_row
    }
}