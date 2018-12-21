package com.project.gcssns.gcssns.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.project.gcssns.gcssns.MainActivity
import com.project.gcssns.gcssns.R
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
        if(MainActivity.currentUser!!.uid != m_galleryArray[position]!!.user!!.uid){
            view.imageButton_gallery_item_viewpager_menu.visibility = View.INVISIBLE
        }
        view.imageButton_gallery_item_viewpager_menu.setOnClickListener {
            var popupMenu = PopupMenu(m_context!!, view.imageButton_gallery_item_viewpager_menu)
            popupMenu.menuInflater.inflate(R.menu.content_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    when(item!!.title) {
                        "수정하기" -> {
                            Toast.makeText(m_context!!, item!!.title, Toast.LENGTH_LONG).show()
                            return true
                        }
                        "삭제하기" -> {
                            var builder = AlertDialog.Builder(m_context!!)
                            builder.setMessage("정말 삭제하시겠습니까?")
                                    .setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                                    .setPositiveButton("예", DialogInterface.OnClickListener {
                                        dialog, which ->  deleteItem(m_galleryArray[position]!!.id)
                                        var g = m_context as Activity
                                        g.finish()
                                    })
                            builder.show()
                            return true
                        }
                    }
                    return false
                }
            })
            popupMenu.show()
        }
        return view
    }

    private fun deleteItem(id : String?){
        Toast.makeText(m_context!!, "게시물이 삭제되었습니다.", Toast.LENGTH_LONG).show()
        val ref = FirebaseDatabase.getInstance().getReference("/gallery/$id").setValue(null)
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