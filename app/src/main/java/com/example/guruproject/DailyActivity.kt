package com.example.guruproject

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


/* 페이지 6번 - 날짜별 activity*/
private const val NUM_PAGES = 3

class DailyActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager
    private val viewModel: DailyViewModel by viewModels()
    private lateinit var curDay: Day
    private lateinit var yesterDay: Day
    private lateinit var nextDay: Day

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        // pager
        mPager = findViewById(R.id.view_pager)
        // 날짜 갱신
        var year: Int = 0
        var month: Int = 0
        var date: Int = 0
        year = intent.getIntExtra("year", 0)
        month = intent.getIntExtra("month", 0)
        date = intent.getIntExtra("date", 0)
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, date)
        curDay = Day(year, month + 1, date)
        calendar.add(Calendar.DATE, -1)
        yesterDay = Day(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DATE)
        )
        calendar.add(Calendar.DATE, 2)
        nextDay = Day(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DATE)
        )
        Log.d("calender", "yester: " + yesterDay.toString())
        Log.d("calender", "next: " + nextDay.toString())

        val pagerAdapter = ScreenSlidePagerAdapter(
            LayoutInflater.from(this@DailyActivity),
            yesterDay,
            curDay,
            nextDay
        )
        mPager.adapter = pagerAdapter
        mPager.setCurrentItem(1, true)
    }

    //팝업
    private fun showSettingPopup(pageyear: Int, pagemonth: Int, pagedate: Int) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.daily_add_popup, null)
        val spinner: Spinner = view.findViewById(R.id.daily_spinner)
        val daily_pop_content = view.findViewById<TextView>(R.id.daily_pop_content)
        var diolgcategory: String = ""
        var diolgcontent: String = ""
        ArrayAdapter.createFromResource(
            this,
            R.array.daily_popup_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        diolgcategory = "토지"
                    }
                    1 -> {
                        diolgcategory = "물"
                    }
                    2 -> {
                        diolgcategory = "공기"
                    }
                    else -> {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(resources.getString(R.string.add))
            .setPositiveButton(resources.getString(R.string.save)) { dialog, which ->
                diolgcontent = daily_pop_content.text.toString()
                viewModel.addTodo(Todo(pageyear, pagemonth, pagedate, diolgcategory, diolgcontent))
            }
            .setNeutralButton(resources.getString(R.string.cancel), null)
            .create()
        alertDialog.setView(view)
        alertDialog.show()
        alertDialog.window?.setLayout(1000, 1200)
    }

    // pager adapter
    private inner class ScreenSlidePagerAdapter(
        val layoutInflater: LayoutInflater
        , var yesterDay: Day, var curDay: Day, var nextDay: Day
    ) :
        PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            when (position) {
                0 -> {
                    return pagedraw(
                        R.layout.activity_daliy_fragment1, container, R.id.daily_date1, yesterDay
                        , R.id.daily_recycler_view1, R.id.daily_add1, R.id.daily_ok1
                    )
                }
                1 -> {
                    return pagedraw(
                        R.layout.activity_daliy_fragment2, container, R.id.daily_date2, curDay
                        , R.id.daily_recycler_view2, R.id.daily_add2, R.id.daily_ok2
                    )
                }
                2 -> {
                    return pagedraw(
                        R.layout.activity_daliy_fragment3, container, R.id.daily_date3, nextDay
                        , R.id.daily_recycler_view3, R.id.daily_add3, R.id.daily_ok3
                    )
                }
                else -> {
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment1, container, false)
                    container.addView(view)
                    return view
                }
            }
        }
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as View
        }
        override fun getCount(): Int {
            return NUM_PAGES
        }

        // page에 관한 모든 일
        fun pagedraw(viewlayout:Int, container:ViewGroup,textId:Int,day: Day
                     ,recyId:Int, addBtnId:Int,okBtnId:Int): View{
            val view =
                layoutInflater.inflate(viewlayout, container, false)
            val textView = view.findViewById<TextView>(textId)
            textView.setText(
                "${day.month}${resources.getString(R.string.month)} ${day.date}${resources.getString(
                    R.string.date
                )}"
            )

            // recyclerView
            var recy = view.findViewById<RecyclerView>(recyId)
            recy.apply {
                layoutManager = LinearLayoutManager(this@DailyActivity)
                adapter = DailyAdapter(emptyList(),
                    onClickDeleteIcon = {
                        viewModel.deleteTodo(it)
                        Log.d("daily", it.getString("content"))
                    }
                )
            }
            // 관찰 UI 업데이트
            viewModel.tododata1.observe(this@DailyActivity, Observer {
                var curList = ArrayList<DocumentSnapshot>()
                for (item in it) {
                    if (((item.getLong("month"))!!.toInt() == day.month) and ((item.getLong("date"))!!.toInt() == day.date)) {
                        curList.add(item)
                    }
                }
                (recy.adapter as DailyAdapter).setData(curList)
            })

            // button
            var add_btn1 = view.findViewById<Button>(addBtnId)
            var ok_btn1 = view.findViewById<Button>(okBtnId)
            add_btn1.setOnClickListener {
                showSettingPopup(day.year,day.month, day.date)
            }
            ok_btn1.setOnClickListener {
                finish()
            }
            container.addView(view)
            return view
        }
    }
}

// recyclerView adapter
class DailyAdapter(
    private var myDataset: List<DocumentSnapshot>,
    val onClickDeleteIcon: (todo: DocumentSnapshot) -> Unit
) :
    RecyclerView.Adapter<DailyAdapter.DailyViewHolder>() {

    class DailyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyAdapter.DailyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daily_content_recyclerview, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val todo = myDataset[position]
        val viewtext = holder.view.findViewById<TextView>(R.id.daily_recy_text)
        val viewcontent = holder.view.findViewById<TextView>(R.id.daily_recy_content)
        viewtext.text = todo.get("category").toString()
        viewcontent.text = todo.get("content").toString()

        val deleteButton = holder.view.findViewById<ImageButton>(R.id.daily_recy_delete)
        deleteButton.setOnClickListener {
            onClickDeleteIcon.invoke(todo) //onclickdeletIcon을 실행함
        }
    }

    override fun getItemCount() = myDataset.size

    fun setData(newData: List<DocumentSnapshot>) {
        myDataset = newData
        notifyDataSetChanged() // 화면 갱신
    }
}

//data관리
class DailyViewModel : ViewModel() {
    val db = Firebase.firestore
    val tododata1 = MutableLiveData<List<DocumentSnapshot>>()
    private val data = arrayListOf<Todo>()

    init {
        fetchData()
    }

    fun fetchData() {
        val user = auth.currentUser
        if (user != null) {
            db.collection(user.uid)
                .document(user.uid)
                .collection("todo")
                .addSnapshotListener { value, e ->
                    if (e != null) { //에러가 나면
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        tododata1.value = value.documents
                    }
                    Log.d("daily", "db: " + tododata1)
                    Log.d("daily", "db auth: " + auth)
                }
        }

    }

    fun addTodo(todo: Todo) {
        auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
            db.collection(user.uid).document(user.uid).collection("todo").add(todo)
        }
        Log.d("daily", "add: " + data.toString())
        Log.d("daily", "" + tododata1.value)
    }

    fun deleteTodo(todo: DocumentSnapshot) {
        auth.currentUser?.let { user ->
            db.collection(user.uid).document(user.uid).collection("todo").document(todo.id).delete()
        }
        Log.d("daily", "delete: " + data.toString())
    }
}
