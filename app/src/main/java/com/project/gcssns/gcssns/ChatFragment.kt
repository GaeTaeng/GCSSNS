package com.project.gcssns.gcssns

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.project.gcssns.gcssns.model.ChatMessage
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_chatlist_row.view.*
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * Created by i412 on 2018-10-29.
 */

class ChatFragment : Fragment(){

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenForLatestMessage()
        recyclerview_chat_chatlist.adapter = adapter
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private  fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(chatListRow(it))
        }
    }

    private fun listenForLatestMessage(){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val chatMessage = p0!!.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val chatMessage = p0!!.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key] = chatMessage
                refreshRecyclerViewMessages()
            }
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot?) {}
        })
    }
}

class chatListRow(val chatMessage: ChatMessage?) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_chatlist_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_chatlist_message.text = chatMessage!!.text
        //viewHolder.itemView.textView_chat_chatlist_username.text = chatMessage!!.
    }
}