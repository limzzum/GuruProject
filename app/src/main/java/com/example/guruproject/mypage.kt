package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_mypage.*

class mypage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val button1: ImageButton = findViewById(R.id.tree)
        val button2: ImageButton = findViewById(R.id.calendar)
        val button3: ImageButton = findViewById(R.id.community)

        button1.setOnClickListener {
            val intent = Intent(this@mypage, TreeActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this@mypage, CalendarActivity::class.java)
            startActivity(intent)
        }


        button3.setOnClickListener {
            val intent = Intent(this@mypage, RecyclerView::class.java)
            startActivity(intent)
        }

        //회원 탈퇴
        quit.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "회원탈퇴가 완료되었습니다", Toast.LENGTH_LONG).show()

                    //로그아웃처리
                    FirebaseAuth.getInstance().signOut()
                    //LoginManager.getInstance().logOut()
                    finish()

                    //회원 가입화면으로 돌아가기
                    val intent = Intent(this@mypage, CreateUserActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}