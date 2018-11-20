package com.project.gcssns.gcssns

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.gcssns.gcssns.model.HomeFeed
import com.squareup.picasso.Picasso
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_home_write.*
import java.io.File
import java.util.*

class HomeWriteActivity : AppCompatActivity() {

    var picturePathList = ArrayList<String>()
    var pictureDownloadPathList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_write)

        val user = MainActivity.currentUser
        textView_home_write_username.text = user!!.userName
        val targetImageView = imageView_home_write_user_image
        Picasso.get().load(user!!.profileImageUrl).into(targetImageView)

        val storagePermission = Manifest.permission.READ_EXTERNAL_STORAGE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(storagePermission), 0)
        }

        imageView_home_write_attach_image.setOnClickListener {
            picturePathList.clear()
            pictureDownloadPathList.clear()
            FilePickerBuilder.getInstance().setMaxCount(20)
                    .setSelectedFiles(picturePathList)
                    .setActivityTheme(R.style.LibAppTheme)
                    .pickPhoto(this)
        }
        button_home_write_submit.setOnClickListener {
            saveDataBaseImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            FilePickerConst.REQUEST_CODE_PHOTO -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    picturePathList = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
                    for(x in picturePathList){
                        uploadImage(Uri.fromFile(File(x)))
                    }
                }
            }
        }
    }

    fun uploadImage(selectedPhotoUri : Uri){
        val filename = UUID.randomUUID().toString() //고유한 파일이름을 만듬
        val ref = FirebaseStorage.getInstance().getReference("images/$filename") //Firebase Storage 래퍼런스 정보
        ref.putFile(selectedPhotoUri) //이미지 파일추가
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        pictureDownloadPathList.add(it.toString())
                    }
                }
    }

    fun saveDataBaseImage(){
        if(pictureDownloadPathList.size == picturePathList.size){
            val ref = FirebaseDatabase.getInstance().getReference("/home").push() //Firebase database 래퍼런스 정보
            val homeFeed = HomeFeed(MainActivity.currentUser!!, editText_home_write_content.text.toString(), pictureDownloadPathList, System.currentTimeMillis() / 1000)
            ref.setValue(homeFeed) // Firebase에서 가져온 래퍼런스 정보에다가 유저 정보를 넣음
                    .addOnSuccessListener {
                        Toast.makeText(this, "메인 게시물 등록이 완료되었습니다.", Toast.LENGTH_LONG).show()
                        Log.d("HomeWrite", "the home picture Information saved")
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "메인 게시물 등록에 실패하였습니다.", Toast.LENGTH_LONG).show()
                        Log.d("HomeWrite", "Failed to save the gallery picture information.")
                    }
        } else{
            Toast.makeText(this, "사진 등록이 완료되지 않았습니다. 잠시만 기다려 주세요.", Toast.LENGTH_LONG).show()
        }
    }

}
