package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        tree.setOnClickListener {
            val intent1 = Intent(this@CalendarActivity, TreeActivity::class.java)
            startActivity(intent1)

        }

        calendar.setOnClickListener {
            val intent = Intent(this@CalendarActivity, CalendarActivity::class.java)
            startActivity(intent)
            finish()
        }


        community.setOnClickListener {
            //Intent2 대신 커뮤니티메뉴
            val intent = Intent(this@CalendarActivity, RecyclerView::class.java)
            startActivity(intent)
        }

        //주석 부분 전부 수정 필요! Intent 연결해야함!
//        my_info.setOnClickListener {
            //Intent2 대신 내 정보메뉴
//            val intent = Intent(this@Calendar_example, Intent2::class.java)
//            startActivity(intent)
//        }
    }
}