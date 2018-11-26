package com.project.gcssns.gcssns

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.project.gcssns.gcssns.model.HomeFeed
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_feed_row.view.*

/**
 * Created by i412 on 2018-10-29.
 */

class HomeFragment : Fragment(){

    val adapter = GroupAdapter<ViewHolder>()

    companion object {
        val HOME_ITEM = "homeItem"
        val HOMEFRAGMENT_REQUEST_CODE_HOMEFEED_UP_POSITION = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton_home_write.setOnClickListener {
            val intent = Intent(context, HomeWriteActivity::class.java)
            intent.putExtra("adapterSize", adapter.itemCount)
            activity!!.startActivityForResult(intent, HOMEFRAGMENT_REQUEST_CODE_HOMEFEED_UP_POSITION)
        }
        recyclerview_home_list.adapter = adapter
        recyclerview_home_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        listenForHomeFeeds()
    }

    private fun listenForHomeFeeds(){
        val ref = FirebaseDatabase.getInstance().getReference("/home")

        ref.addChildEventListener(object: ChildEventListener { //데이터 베이스에서 메세지 정보를 찾아서 뷰에 표시해 준다
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val homeItem = p0!!.getValue(HomeFeed::class.java)
                if(homeItem != null && MainActivity.currentUser!!.userMajor == homeItem.user!!.userMajor){
                    adapter.add(HomeFeedItem(homeItem))
                }
                adapter.setOnItemClickListener { item, view ->
                    var homeFeedItem = item as HomeFeedItem
                    val intent = Intent(context, HomeReadActivity::class.java)
                    intent.putExtra(HomeFragment.HOME_ITEM, homeFeedItem._homeFeed)
                    startActivity(intent)
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

}

class HomeFeedItem(var homeFeed : HomeFeed) : Item<ViewHolder>(){

    var _homeFeed : HomeFeed? = null

    override fun bind(viewHolder: ViewHolder, position: Int) {
        _homeFeed = homeFeed
        viewHolder.itemView.textView_home_feed_username.text = homeFeed.user!!.userName
        viewHolder.itemView.editText_home_feed_content.setText(homeFeed.text)
        val targetImage = viewHolder.itemView.imageView_home_feed_user_image
        Picasso.get().load(homeFeed.user!!.profileImageUrl).into(targetImage)
    }

    override fun getLayout(): Int {
        return R.layout.home_feed_row
    }

}