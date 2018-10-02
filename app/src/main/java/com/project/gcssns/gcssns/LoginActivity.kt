package com.project.gcssns.gcssns

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        button_login_email.setOnClickListener {
            emailLogin()
        }
        textView_login_signup.setOnClickListener {
            moveSignupActivity()
        }
    }

    fun moveSignupActivity(){
        startActivity(Intent(this, SignupActivity::class.java))
    }

    fun emailLogin(){
        if(editText_login_email.text.toString() == "" || editText_login_password.text.toString() ==  ""){
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            return
        }
        auth!!.signInWithEmailAndPassword(editText_login_email.text.toString(), editText_login_password.text.toString()).addOnCompleteListener {task ->
            if(task.isSuccessful){
                Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_LONG).show()
                moveMainActivity()
            } else{
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    fun moveMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
    }

}
