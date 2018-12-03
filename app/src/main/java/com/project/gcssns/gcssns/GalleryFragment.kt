package com.project.gcssns.gcssns

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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

    companion object {
        val GALLERY_ITEM_POSITION = "galleryItemPoistion"
    }

    val adapter = GroupAdapter<ViewHolder>()
    var width : Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortDataBaseGalleryItem()
        recyclerview_gallery_list.adapter = adapter
        //adapter.add(GalleryListItem(width))
        recyclerview_gallery_list.layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        //recyclerview_gallery_list.layoutManager = gridLayout
        width = resources.displayMetrics.widthPixels / 3
        val re = LinearLayout(context)
        listenForGallerys()
        floatingActionButton_gallery_write.setOnClickListener {
            var intent = Intent(context, GalleryWriteActivity::class.java)
            startActivity(intent)
        }
    }

    fun sortDataBaseGalleryItem(){
        Toast.makeText(context, "정렬", Toast.LENGTH_LONG).show()
        val ref = FirebaseDatabase.getInstance().getReference("/gallery").orderByChild("text")
    }

    private fun listenForGallerys(){
        val ref = FirebaseDatabase.getInstance().getReference("/gallery")

        ref.addChildEventListener(object: ChildEventListener { //데이터 베이스에서 메세지 정보를 찾아서 뷰에 표시해 준다
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val galleryItem = p0!!.getValue(GalleryPicture::class.java)
                if(galleryItem != null){
                    adapter.add(0, GalleryListItem(galleryItem, width!!))
                }
                adapter.setOnItemClickListener { item, view ->
                    val galleryItem = item as GalleryPicture
                    val intent = Intent(context, GalleryReadActivity::class.java)
                    val index = adapter.getAdapterPosition(item)
                    intent.putExtra(GALLERY_ITEM_POSITION, index)
                    //Toast.makeText(context, item.getPosition(item).toString(), Toast.LENGTH_LONG).show()
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
                adapter.notifyDataSetChanged()
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
        viewHolder.itemView.linearLayout_gallery_picture_row.layoutParams.width = width
        viewHolder.itemView.linearLayout_gallery_picture_row.layoutParams.height = width
        val targetImageView = viewHolder.itemView.imageView_gallery_picture
        Picasso.get().load(galleryPictureUrl).into(targetImageView)
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}