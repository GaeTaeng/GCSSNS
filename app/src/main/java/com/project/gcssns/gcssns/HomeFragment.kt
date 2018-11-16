package com.project.gcssns.gcssns

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GroupAdapter<ViewHolder>()

        floatingActionButton_home_write.setOnClickListener {
            val intent = Intent(context, HomeWriteActivity::class.java)
            startActivity(intent)
        }
        recyclerview_home_list.adapter = adapter
        recyclerview_home_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

}

class HomeFeedItem(var homeFeed : HomeFeed) : Item<ViewHolder>(){

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_home_feed_username.text = homeFeed.user!!.userName
        val targetImage = viewHolder.itemView.imageView_home_feed_user_image
        Picasso.get().load(homeFeed.user!!.profileImageUrl).into(targetImage)
    }

    override fun getLayout(): Int {
        return R.layout.home_feed_row
    }

}