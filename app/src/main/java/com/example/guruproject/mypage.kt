package com.example.guruproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Log.d
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_mypage.*

class mypage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)
val Id=findViewById<TextView>(R.id.user)
         Id.setText(auth.currentUser.toString())

       // (FirebaseAuth.getInstance().currentUser)

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

        //로그아웃
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "로그아웃이 완료되었습니다", Toast.LENGTH_SHORT).show()

            //로그인 화면으로 돌아가기
            val intent = Intent(this@mypage, LoginActivity::class.java)
            startActivity(intent)
        }

        //회원 탈퇴
        quit.setOnClickListener {
            showSettingPopup()
        }
    }

    //계정 삭제 함수
    fun deleteUser() {
        FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "회원탈퇴가 완료되었습니다", Toast.LENGTH_LONG).show()

                //로그아웃처리
                FirebaseAuth.getInstance().signOut()
                finish()

                //회원 가입화면으로 돌아가기
                val intent = Intent(this@mypage, CreateUserActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()

            }
        }
    }

    //탈퇴 팝업창 함수
    private fun showSettingPopup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.activity_alert_popup, null)
        val textView: TextView = view.findViewById(R.id.textView)
        textView.text = "정말 탈퇴하시겠습니까?"

        val alertDialog = AlertDialog.Builder(this)
            .setPositiveButton("확인") { dialog, which ->
                Toast.makeText(this, "탈퇴를 진행합니다", Toast.LENGTH_SHORT).show()
                deleteUser()
            }
            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }
}
