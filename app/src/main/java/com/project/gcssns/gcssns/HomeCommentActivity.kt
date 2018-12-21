package com.project.gcssns.gcssns

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.project.gcssns.gcssns.fragment.HomeFragment
import com.project.gcssns.gcssns.model.HomeFeed
import com.project.gcssns.gcssns.model.HomeFeedComment
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home_comment.*
import kotlinx.android.synthetic.main.home_comment_row.view.*
import java.util.*


class HomeCommentActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()
    var homefeed: HomeFeed? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_comment)

        recyclerview_home_comment_list.adapter = adapter

        homefeed = intent.getParcelableExtra<HomeFeed>(HomeFragment.HOME_ITEM)

        imageView_home_comment_send.setOnClickListener {
            writeComment()
        }
       /* editText_home_comment_message.setOnClickListener {
            recyclerview_home_comment_list.scrollToPosition(adapter.itemCount - 1)
        }*/
        listenForComments()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (newConfig!!.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
        } else if (newConfig!!.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
        }
    }

    fun writeComment() {
        if (editText_home_comment_message.text.isEmpty()) return
        var id = UUID.randomUUID().toString()
        val homeFeedItem = HomeFeedComment(id, homefeed!!.id, editText_home_comment_message.text.toString(), MainActivity.currentUser, System.currentTimeMillis() / 1000)
        val ref = FirebaseDatabase.getInstance().getReference("/home/${homefeed!!.id}/comments/$id")
        ref.setValue(homeFeedItem)
        editText_home_comment_message.text = null
    }

    private fun listenForComments() {
            val ref = FirebaseDatabase.getInstance().getReference("/home/${homefeed!!.id}/comments")

            ref.addChildEventListener(object : ChildEventListener {
                //데이터 베이스에서 메세지 정보를 찾아서 뷰에 표시해 준다
                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    val homeFeedItem = p0!!.getValue(HomeFeedComment::class.java)
                    adapter.add(HomeFeedCommentItem(homeFeedItem!!))
                    recyclerview_home_comment_list.scrollToPosition(adapter.itemCount - 1)
                }
                override fun onCancelled(p0: DatabaseError?) {}

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}

            override fun onChildRemoved(p0: DataSnapshot?) {}
        })

    }

}

class HomeFeedCommentItem(var homeComment : HomeFeedComment) : Item<ViewHolder>(){

    var _homeComment : HomeFeedComment? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        _homeComment = homeComment
        //viewHolder.itemView.textView_home_feed_username.text = homeFeed.user!!.userName
        viewHolder.itemView.textView_home_comment_row_message.text = homeComment.text
        val targetImage = viewHolder.itemView.imageView_home_comment_row_user_image
        Picasso.get().load(homeComment.user!!.profileImageUrl).into(targetImage)
    }

    override fun getLayout(): Int {
        return R.layout.home_comment_row
    }

}