package com.project.gcssns.gcssns

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.gcssns.gcssns.model.GalleryPicture
import kotlinx.android.synthetic.main.activity_gallery_read.*

class GalleryReadActivity : AppCompatActivity() {

   //val adapter = PagerAdapter<ViewHolder>()
    var galleryList = arrayListOf<GalleryPicture?>()
    var adapter : GalleryPagerAdapter? = null
    var positionItem : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_read)
        patchGalleryItem()

        positionItem = intent.getIntExtra(GalleryFragment.GALLERY_ITEM_POSITION,0)
        adapter = GalleryPagerAdapter(this)
        viewPager_gallery_read.adapter = adapter
        Handler().post {
            kotlin.run {
                viewPager_gallery_read.currentItem = positionItem!!
            }
        }
    }

    override fun onResume() {
        Handler().post {
            kotlin.run {
                viewPager_gallery_read.currentItem = positionItem!!
            }
        }
        super.onResume()
    }


    private fun patchGalleryItem(){ //데이터 베이스에서 사진들을 가져와 recycler로 보여준다
        val ref = FirebaseDatabase.getInstance().getReference("/gallery")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot?) {
                p0!!.children.forEach{
                    val galleryItem = it.getValue(GalleryPicture::class.java)
                    if(galleryItem != null){
                        adapter!!.addItem(galleryItem)
                        println("테스트 중 : " + galleryList)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError?) {}
        })
    }



}
