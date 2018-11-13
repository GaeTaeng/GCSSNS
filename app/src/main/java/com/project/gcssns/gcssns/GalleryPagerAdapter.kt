package com.project.gcssns.gcssns

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.gcssns.gcssns.model.GalleryPicture
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gallery_item_viewpager.view.*

class GalleryPagerAdapter : PagerAdapter{

    var m_context : Context? = null
    var m_galleryArray = ArrayList<GalleryPicture?>()
    var m_inflater : LayoutInflater? = null

    constructor(context : Context) : super(){
        this.m_context = context
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1 as ConstraintLayout
    }

    override fun getCount(): Int {
        return m_galleryArray.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        m_inflater = m_context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view = m_inflater!!.inflate(R.layout.gallery_item_viewpager, container, false)
        val imageTarget = view.imageView_gallery_item_viewpager_main_image
        Picasso.get().load(m_galleryArray[position]!!.picutreImageUrl).into(imageTarget)
        view.textView_gallery_item_viewpager_username.text = m_galleryArray[position]!!.user!!.userName
        view.editText_gallery_item_viewpager_content.setText(m_galleryArray[position]!!.text.toString())
        val profileImageTarget = view.imageView_gallery_item_viewpager_user_image
        Picasso.get().load(m_galleryArray[position]!!.user!!.profileImageUrl).into(profileImageTarget)
        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container!!.removeView(`object` as ConstraintLayout)
    }

    fun addItem(galleyPicture : GalleryPicture?){
        m_galleryArray.add(galleyPicture)
        notifyDataSetChanged()
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
    }
}