package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_calendar.*

class mypage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        tree.setOnClickListener {
            val intent1 = Intent(this@mypage, TreeActivity::class.java)
            startActivity(intent1)

        }

        calendar.setOnClickListener {
            val intent = Intent(this@mypage, CalendarActivity::class.java)
            startActivity(intent)
        }


        community.setOnClickListener {
            //Intent2 대신 커뮤니티메뉴
            val intent = Intent(this@mypage, RecyclerView::class.java)
            startActivity(intent)
        }
    }
}