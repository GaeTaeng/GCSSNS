package com.project.gcssns.gcssns

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.gcssns.gcssns.model.GalleryPicture
import kotlinx.android.synthetic.main.activity_gallery_write.*
import java.util.*

class GalleryWriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery_write)

        button_gallery_write_upload_image.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
        button_gallery_write_submit.setOnClickListener {
            uploadImage()
        }
    }

    var selectedPhotoUri : Uri? = null

    fun uploadImage(){
        Toast.makeText(this, selectedPhotoUri.toString(), Toast.LENGTH_LONG).show()
        if(selectedPhotoUri == null) {
            Toast.makeText(this, "사진을 선택해 주세요.", Toast.LENGTH_LONG).show()
            return
        }
        if(editText_gallery_write_content.text.isEmpty()){
            Toast.makeText(this, "내용을 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }
        val filename = UUID.randomUUID().toString() //고유한 파일이름을 만듬
        val ref = FirebaseStorage.getInstance().getReference("images/$filename") //Firebase Storage 래퍼런스 정보
        ref.putFile(selectedPhotoUri!!) //이미지 파일추가
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("GalleryPictureWrite", "File Location : $it")
                        saveDataBaseImage(it.toString(), filename)
                        sortDataBaseGalleryItem()
                    }
                }
    }

    fun sortDataBaseGalleryItem(){
        val ref = FirebaseDatabase.getInstance().getReference("/gallery").orderByChild("text")
    }

    fun saveDataBaseImage(selectedPhotoUri: String, filename : String){
        val uid = FirebaseAuth.getInstance().uid //유저 고유 아이디
        val ref = FirebaseDatabase.getInstance().getReference("/gallery").push() //Firebase database 래퍼런스 정보
        val galleryPicture = GalleryPicture(filename, MainActivity.currentUser!!, editText_gallery_write_content.text.toString(), selectedPhotoUri,System.currentTimeMillis() / 1000)
        println("유저 정보 : " + galleryPicture)
        ref.setValue(galleryPicture) // Firebase에서 가져온 래퍼런스 정보에다가 유저 정보를 넣음
                .addOnSuccessListener {
                    Toast.makeText(this, "사진 등록이 완료되었습니다.", Toast.LENGTH_LONG).show()
                    Log.d("GalleryPictureWrite", "the gallery picture Information saved")
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "사진 등록에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    Log.d("GalleryPictureWrite", "Failed to save the gallery picture information.")
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            imageView_gallery_write_image.setImageBitmap(bitmap)
            button_gallery_write_upload_image.alpha = 0f
        }
    }

}
