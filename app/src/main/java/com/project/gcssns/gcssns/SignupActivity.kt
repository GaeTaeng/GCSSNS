package com.project.gcssns.gcssns

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("SignupActivity", "File Location : $it")
                        saveUserDataToDatabase(selectedPhotoUri.toString())
                    }
                }
    }

    fun saveUserDataToDatabase(selectedPhotoUri: String){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, selectedPhotoUri, editText_signup_email.text.toString(), editText_signup_name.text.toString(), editText_signup_studentId.text.toString(), editText_signup_major.text.toString())
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("SignupActivity", "User Information saved")
                }
    }

    var selectedPhotoUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitamapDrawable = BitmapDrawable(bitmap)
            button_signup_photo.setBackgroundDrawable(bitamapDrawable)
        }
    }
}