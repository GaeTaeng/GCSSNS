package com.project.gcssns.gcssns.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.gcssns.gcssns.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.home_picture_item_viewpager.view.*

class HomeFeedImagePagerAdapter : PagerAdapter {

    var m_context : Context? = null
    var m_homeFeedImageArray = ArrayList<String?>()
    var m_inflater : LayoutInflater? = null

    constructor(context : Context) : super(){
        this.m_context = context
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1 as ConstraintLayout
    }

    override fun getCount(): Int {
        return m_homeFeedImageArray.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        m_inflater = m_context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = m_inflater!!.inflate(R.layout.home_picture_item_viewpager, container, false)
        val imageTarget = view.imageView_home_picture_item_image
        Picasso.get().load(m_homeFeedImageArray[position]!!).into(imageTarget)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as ConstraintLayout)
    }

    fun addItem(homeFeed : String?){
        m_homeFeedImageArray.add(homeFeed)
        notifyDataSetChanged()
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
    }
}