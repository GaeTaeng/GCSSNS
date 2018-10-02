package com.project.gcssns.gcssns

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        button_signup_submit.setOnClickListener {
            signup()
        }
    }

    fun signup(){
        if(editText_signup_email.text.toString().isEmpty() || editText_signup_password.text.toString().isEmpty() || editText_signup_name.text.toString().isEmpty()
        || editText_signup_major.text.toString().isEmpty() || editText_signup_studentId.toString().isEmpty()){
            Toast.makeText(this, "모든 정보를 입력해 주세요.", Toast.LENGTH_LONG).show()
            return
        }
        auth!!.createUserWithEmailAndPassword(editText_signup_email.text.toString(), editText_signup_password.text.toString()).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Toast.makeText(this,"회원가입에 성공했습니다.", Toast.LENGTH_LONG).show()
                finish()
            } else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}
