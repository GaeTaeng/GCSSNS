package com.project.gcssns.gcssns

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    var auth : FirebaseAuth? = null
    var user : FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // FirebaseAuthentication에서 정보를 받아옴
        auth = FirebaseAuth.getInstance()

        user = auth!!.currentUser //현재 로그인 된 유저 정보
        moveMainActivity() //유저 정보가 있으면 mainAcitivty 이동 시도

        button_login_email.setOnClickListener {
            emailLogin()
        }
        textView_login_signup.setOnClickListener {
            moveSignupActivity()
        }
    }

    private fun moveSignupActivity(){ //회원 가입 페이지로 이동
        startActivity(Intent(this, SignupActivity::class.java))
    }

    override fun onBackPressed() {
        var builder = AlertDialog.Builder(this)
        builder.setMessage("앱을 종료 하시겠습니까?")
                .setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which -> appFinish() })
        builder.show()
    }

    fun appFinish(){
        super.onBackPressed()
    }


    private fun emailLogin(){
        if(editText_login_email.text.toString() == "" || editText_login_password.text.toString() ==  ""){
            Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show()
            return
        }
        //이메일 로그인 하는 함수  signInWithEmailAndPassword(email, password)
        auth!!.signInWithEmailAndPassword(editText_login_email.text.toString(), editText_login_password.text.toString()).addOnCompleteListener {task ->
            if(task.isSuccessful){  //성공시 메인 액티비티 이동
                Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_LONG).show()
                user = auth!!.currentUser
                moveMainActivity()
            } else{ //실패시 오류 출력
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun moveMainActivity(){
        if(user != null){ //유저 정보가 있으면 메인 액티비티로 이동
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

}
