package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_calendar.*

/*5번 화면*/
class CalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // 달력 날짜가 선택되면

            //6번 화면과 연결하는 부분
            val intent = Intent(this@CalendarActivity, DailyActivity::class.java)
            intent.putExtra("year",year)
            intent.putExtra("month", month)
            intent.putExtra("date",dayOfMonth)
            startActivity(intent)

        }

        // 나무 메뉴가 선택되면
        val button1: ImageButton =findViewById(R.id.tree)
        val button2: ImageButton =findViewById(R.id.calendar)
        val button3: ImageButton =findViewById(R.id.community)
        val button4: ImageButton =findViewById(R.id.my_info)

        button1.setOnClickListener{
            val intent = Intent(this@CalendarActivity,TreeActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this@CalendarActivity, CalendarActivity::class.java)
            startActivity(intent)
        }


        button3.setOnClickListener {
            val intent = Intent(this@CalendarActivity, com.example.guruproject.RecyclerView::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener {
            val intent = Intent(this@CalendarActivity, mypage::class.java)
            startActivity(intent)
        }
    }
}