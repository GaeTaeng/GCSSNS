package com.project.gcssns.gcssns

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home_picture_read.*

class HomePictureReadActivity : AppCompatActivity() {

    var adapter : HomeFeedPagerAdapter? = null
    var homePictureArray : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_picture_read)
        homePictureArray = intent.getStringArrayListExtra(HomeReadActivity.HOME_READ_PICTURE_ITEM)
        adapter = HomeFeedPagerAdapter(this)
        viewPager_home_picture_read.adapter = adapter
        for(x in homePictureArray!!){
            adapter!!.addItem(x)
        }
    }
}
