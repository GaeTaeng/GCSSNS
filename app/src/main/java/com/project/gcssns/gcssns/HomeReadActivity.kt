package com.project.gcssns.gcssns

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.project.gcssns.gcssns.model.HomeFeed
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home_read.*

class HomeReadActivity : AppCompatActivity() {

    var homefeed : HomeFeed? = null

    companion object {
        val HOME_READ_PICTURE_ITEM = "homeReadPictureItem"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_read)

        homefeed = intent.getParcelableExtra<HomeFeed>(HomeFragment.HOME_ITEM)

        if(homefeed!!.picutreImageUrls == null){
            imageView_home_read_view_image.visibility = View.INVISIBLE
        }
        imageView_home_read_view_image.setOnClickListener {
            val intent = Intent(this, HomePictureReadActivity::class.java)
            intent.putStringArrayListExtra(HomeReadActivity.HOME_READ_PICTURE_ITEM, homefeed!!.picutreImageUrls)
            startActivity(intent)
        }

        textView_home_read_username.text = homefeed!!.user!!.userName
        val targetUserImage = imageView_home_read_user_image
        Picasso.get().load(homefeed!!.user!!.profileImageUrl).into(targetUserImage)
        editText_home_read_content.setText(homefeed!!.text)
    }
}