package com.project.gcssns.gcssns

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.gcssns.gcssns.model.GalleryPicture
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.gallery_item_viewpager.view.*

class GalleryPagerAdapter : PagerAdapter{

    var m_context : Context? = null
    var m_galleryArray : Array<GalleryPicture>? = null
    var m_inflater : LayoutInflater? = null

    constructor(context : Context, galleryArray : Array<GalleryPicture>?) : super(){
        this.m_context = context
        this.m_galleryArray = galleryArray
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return false
    }

    override fun getCount(): Int {
        return m_galleryArray!!.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = m_inflater!!.inflate(R.layout.gallery_item_viewpager, container, false)
        val imageTarget = view.imageView_gallery_item_viewpager_main_image
        view.textView_gallery_item_viewpager_username.text = m_galleryArray!![position].userName
        Picasso.get().load(m_galleryArray!![position].picutreImageUrl).into(imageTarget)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}