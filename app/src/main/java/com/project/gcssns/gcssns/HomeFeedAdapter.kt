package com.project.gcssns.gcssns

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.gcssns.gcssns.model.HomeFeed

class HomeFeedAdapter(var homefeedList : ArrayList<HomeFeed>) : RecyclerView.Adapter<HomeFeedAdapter.ViewHolder>() {

    var m_context : Context? = null
    var m_homeFeedArray = ArrayList<String?>()
    var m_inflater : LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        return homefeedList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){


    }


}