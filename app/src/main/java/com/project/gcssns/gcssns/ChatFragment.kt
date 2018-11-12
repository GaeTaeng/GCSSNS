package com.project.gcssns.gcssns

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.project.gcssns.gcssns.model.ChatMessage
import com.project.gcssns.gcssns.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_chatlist_row.view.*
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * Created by i412 on 2018-10-29.
 */

class ChatFragment : Fragment(){

    companion object {
        val TAG = "ChatFragment"
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenForLatestMessage()
        recyclerview_chat_chatlist.adapter = adapter
        recyclerview_chat_chatlist.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        adapter.setOnItemClickListener { item, view -> //
            val chatListItem = item as chatListRow
            Log.d(TAG, "click chatlist adapter")
            val intent = Intent(context, ChatLogActivity::class.java)
            chatListItem.chatPartnerUser
            intent.putExtra(MembersFragment.USER_KEY, chatListItem.chatPartnerUser)
            startActivity(intent)
        }
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private  fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(chatListRow(it))
        }
    }

    private fun listenForLatestMessage(){ //최신 메세지 갱신 HashMap을 이용해서 최신 메세지 내용를 수정함
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

    var chatPartnerUser : User? = null

    override fun getLayout(): Int {
        return R.layout.chat_chatlist_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_chatlist_message.text = chatMessage!!.text
        //iewHolder.itemView.textView_chat_chatlist_username.text = chatMessage!!.fromId
        val chatPartnerId : String?
        if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId
        } else{
            chatPartnerId = chatMessage.fromId
        }

        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {
                chatPartnerUser = p0!!.getValue(User::class.java)
                viewHolder.itemView.textView_chat_chatlist_username.text = chatPartnerUser!!.userName

                val targetImageView = viewHolder.itemView.imageView_chat_chatlist_profile
                Picasso.get().load(chatPartnerUser!!.profileImageUrl).into(targetImageView)
            }
            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }
}