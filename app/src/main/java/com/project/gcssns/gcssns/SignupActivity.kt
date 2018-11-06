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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.gcssns.gcssns.model.User
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null
    var firebaseFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance() // firebase로부터 authentication 정보를 받아옴
        firebaseFirestore = FirebaseFirestore.getInstance() // firebase로부터 database 정보를 받아옴

        button_signup_submit.setOnClickListener {
            signup()
        }

        button_signup_photo.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    fun signup(){
        //모든 정보가 입력 되었나 확인
        if(editText_signup_email.text.toString().isEmpty() || editText_signup_password.text.toString().isEmpty() || editText_signup_name.text.toString().isEmpty()
        || editText_signup_major.text.toString().isEmpty() || editText_signup_studentId.toString().isEmpty()){
            Toast.makeText(this, "모든 정보를 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }
        // 이메일 로그인 유저 만드는 함수 createUserWithEmailAndPassword(email, password)
        auth!!.createUserWithEmailAndPassword(editText_signup_email.text.toString(), editText_signup_password.text.toString()).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Toast.makeText(this,"회원가입에 성공했습니다.", Toast.LENGTH_LONG).show()
                uploadImage()
                finish()
            } else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadImage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString() //고유한 파일이름을 만듬
        val ref = FirebaseStorage.getInstance().getReference("images/$filename") //Firebase Storage 래퍼런스 정보
        ref.putFile(selectedPhotoUri!!) //이미지 파일추가
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("SignupActivity", "File Location : $it")
                        saveUserDataToDatabase(it.toString())
                    }
                }
    }

    fun saveUserDataToDatabase(selectedPhotoUri: String){
        val uid = FirebaseAuth.getInstance().uid //유저 고유 아이디
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid") //Firebase database 래퍼런스 정보
        val user = User(uid, selectedPhotoUri, editText_signup_email.text.toString(), editText_signup_name.text.toString(), editText_signup_studentId.text.toString(), editText_signup_major.text.toString())
        println("유저 정보 : " + user)
        ref.setValue(user) // Firebase에서 가져온 래퍼런스 정보에다가 유저 정보를 넣음
                .addOnSuccessListener {
                    Log.d("SignupActivity", "User Information saved")
                }
                .addOnFailureListener {
                    Log.d("SignupActivity", "Failed to save user information.")
                }
    }

    var selectedPhotoUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            //val bitamapDrawable = BitmapDrawable(bitmap)
            //button_signup_photo.setBackgroundDrawable(bitamapDrawable)
            imageView_signup_photo_register.setImageBitmap(bitmap)
            button_signup_photo.alpha = 0f
        }
    }
}