package com.project.gcssns.gcssns

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.gcssns.gcssns.model.GalleryPicture
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_gallery.*
import kotlinx.android.synthetic.main.gallery_picture_row.view.*

/**
 * Created by i412 on 2018-10-29.
 */

class GalleryFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(inflater.context).inflate(R.layout.fragment_gallery, container, false)

    }

    val adapter = GroupAdapter<ViewHolder>()
    var width : Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview_gallery_list.adapter = adapter
        //adapter.add(GalleryListItem(width))
        recyclerview_gallery_list.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        width = resources.displayMetrics.widthPixels / 3
        //GridLayoutManager()
    }

    private fun fetchGallery(){ //데이터 베이스에서 사진들을 가져와 recycler로 보여준다
        val ref = FirebaseDatabase.getInstance().getReference("/gallery")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            val adapter = GroupAdapter<ViewHolder>()
            override fun onDataChange(p0: DataSnapshot?) {
                p0!!.children.forEach{
                    Log.d("NewPicture", it.toString())
                    val galleryItem = it.getValue(GalleryPicture::class.java)
                    if(galleryItem != null){
                        adapter.add(GalleryListItem(galleryItem, width!!))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    /*val userItem = item as UserItem

                    val intent = Intent(context, ChatLogActivity::class.java)
                    intent.putExtra(MembersFragment.USER_KEY, item.user)
                    startActivity(intent)*/
                }
                recyclerview_gallery_list.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError?) {

            }
        })
    }

}

class GalleryListItem(var galleryPicture: GalleryPicture, var width : Int) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.gallery_picture_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val galleryPictureUrl = galleryPicture.picutreImageUrl
        viewHolder.itemView.linearLayout_gallery_main.layoutParams.width = width
        viewHolder.itemView.linearLayout_gallery_main.layoutParams.height = width
        //viewHolder.itemView.recyclerview_gallery_list.
        val targetImageView = viewHolder.itemView.imageView_gallery_picture
        Picasso.get().load(galleryPictureUrl).into(targetImageView)
    }
}