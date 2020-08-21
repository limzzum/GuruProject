package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_calendar.*

class mypage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val button1:ImageButton=findViewById(R.id.tree)
        val button2:ImageButton=findViewById(R.id.calendar)
        val button3:ImageButton=findViewById(R.id.community)

        button1.setOnClickListener{
            val intent = Intent(this@mypage,TreeActivity::class.java)
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
    }


}