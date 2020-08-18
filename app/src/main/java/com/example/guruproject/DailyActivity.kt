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
import java.util.*
import kotlin.collections.ArrayList


/* 페이지 6번 - 날짜별 activity*/
private const val NUM_PAGES = 3

class DailyActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager
    private val viewModel: DailyViewModel by viewModels()
    private var diolgcategory: String = ""
    private var diolgcontent: String = ""
    private lateinit var curDay: Day
    private lateinit var yesterDay: Day
    private lateinit var nextDay: Day

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        // pager
        mPager = findViewById(R.id.view_pager)
        // 날짜 갱신
        var year: Int=0
        var month: Int=0
        var date: Int=0
        year = intent.getIntExtra("year",0)
        month = intent.getIntExtra("month", 0)
        date = intent.getIntExtra("date", 0)
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, date)
        curDay=Day(year, month+1,date)
        calendar.add(Calendar.DATE, -1)
        yesterDay=Day(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE))
        calendar.add(Calendar.DATE, 2)
        nextDay=Day(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE))
        Log.d("calender", "yester: "+yesterDay.toString())
        Log.d("calender", "next: "+nextDay.toString())

        val pagerAdapter = ScreenSlidePagerAdapter(
            LayoutInflater.from(this@DailyActivity),
            yesterDay,
            curDay,
            nextDay
        )
        mPager.adapter = pagerAdapter
        mPager.setCurrentItem(1, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    //팝업
    private fun showSettingPopup(pageyear:Int, pagemonth: Int, pagedate: Int) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.daily_add_popup, null)
        val spinner: Spinner = view.findViewById(R.id.daily_spinner)
        val daily_pop_content = view.findViewById<TextView>(R.id.daily_pop_content)
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
                viewModel.addTodo(Todo(pageyear,pagemonth, pagedate, diolgcategory, diolgcontent))
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
        , var yesterDay: Day , var curDay: Day, var nextDay: Day
    ) :
        PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            when (position) {
                0 -> {
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment1, container, false)
                    val textView1 = view.findViewById<TextView>(R.id.daily_date1)
                    textView1.setText(
                        "${yesterDay.month}${resources.getString(R.string.month)} ${yesterDay.date}${resources.getString(
                            R.string.date
                        )}"
                    )

                    // recyclerView
                    var recy1 = view.findViewById<RecyclerView>(R.id.daily_recycler_view1)
                    recy1.apply {
                        layoutManager = LinearLayoutManager(this@DailyActivity)
                        adapter = DailyAdapter(emptyList(),
                            onClickDeleteIcon = {
                                viewModel.deleteTodo(it)
                            }
                        )
                    }
                    // 관찰 UI 업데이트
                    viewModel.tododata1.observe(this@DailyActivity, Observer {
                        var curList = ArrayList<Todo>()
                        for (item in it) {
                            if ((item.month == yesterDay.month) and (item.date == yesterDay.date)) {

                                curList.add(item)
                                (recy1.adapter as DailyAdapter).setData(curList)
                            }
                        }
                    })

                    // button
                    var add_btn1 = view.findViewById<Button>(R.id.daily_add1)
                    var ok_btn1 = view.findViewById<Button>(R.id.daily_ok1)
                    add_btn1.setOnClickListener {
                        showSettingPopup(yesterDay.year,yesterDay.month, yesterDay.date)
                    }
                    ok_btn1.setOnClickListener {
                        finish()
                    }
                    container.addView(view)
                    return view
                }
                1 -> {
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment2, container, false)
                    val textView2 = view.findViewById<TextView>(R.id.daily_date2)
                    textView2.setText(
                        "${curDay.month}${resources.getString(R.string.month)} ${curDay.date}${resources.getString(
                            R.string.date
                        )}"
                    )

                    // recyclerView
                    var recy2 = view.findViewById<RecyclerView>(R.id.daily_recycler_view2)
                    recy2.apply {
                        layoutManager = LinearLayoutManager(this@DailyActivity)
                        adapter = DailyAdapter(emptyList(),
                            onClickDeleteIcon = {
                                viewModel.deleteTodo(it)
                            }
                        )
                    }
                    // 관찰 UI 업데이트
                    viewModel.tododata1.observe(this@DailyActivity, Observer {
                        var curList = ArrayList<Todo>()
                        for (item in it) {
                            if ((item.month == curDay.month) and (item.date == curDay.date)) {

                                curList.add(item)
                                (recy2.adapter as DailyAdapter).setData(curList)
                            }
                        }
                    })

                    // button
                    var add_btn2 = view.findViewById<Button>(R.id.daily_add2)
                    var ok_btn2 = view.findViewById<Button>(R.id.daily_ok2)
                    add_btn2.setOnClickListener {
                        showSettingPopup(curDay.year,curDay.month, curDay.date)
                    }
                    ok_btn2.setOnClickListener {
                        finish()
                    }
                    container.addView(view)
                    return view
                }
                2 -> {
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment3, container, false)
                    val textView3 = view.findViewById<TextView>(R.id.daily_date3)
                    textView3.setText(
                        "${nextDay.month}${resources.getString(R.string.month)} ${nextDay.date}${resources.getString(
                            R.string.date
                        )}"
                    )

                    // recyclerView
                    var recy3 = view.findViewById<RecyclerView>(R.id.daily_recycler_view3)
                    recy3.apply {
                        layoutManager = LinearLayoutManager(this@DailyActivity)
                        adapter = DailyAdapter(emptyList(),
                            onClickDeleteIcon = {
                                viewModel.deleteTodo(it)
                            }
                        )
                    }
                    // 관찰 UI 업데이트
                    viewModel.tododata1.observe(this@DailyActivity, Observer {
                        var curList = ArrayList<Todo>()
                        for (item in it) {
                            if ((item.month == nextDay.month) and (item.date == nextDay.date)) {

                                curList.add(item)
                                (recy3.adapter as DailyAdapter).setData(curList)
                            }
                        }
                    })

                    // button
                    var add_btn3 = view.findViewById<Button>(R.id.daily_add3)
                    var ok_btn3 = view.findViewById<Button>(R.id.daily_ok3)
                    add_btn3.setOnClickListener {
                        showSettingPopup(nextDay.year,nextDay.month, nextDay.date)
                    }
                    ok_btn3.setOnClickListener {
                        finish()
                    }
                    container.addView(view)
                    return view
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
    }
}

// recyclerView adapter
class DailyAdapter(
    private var myDataset: List<Todo>,
    val onClickDeleteIcon: (todo: Todo) -> Unit
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
        viewtext.text = myDataset[position].category
        viewcontent.text = myDataset[position].content
        val deleteButton = holder.view.findViewById<ImageButton>(R.id.daily_recy_delete)
        deleteButton.setOnClickListener {
            onClickDeleteIcon.invoke(todo) //onclickdeletIcon을 실행함
        }
    }

    override fun getItemCount() = myDataset.size
    fun setData(newData: List<Todo>) {
        myDataset = newData
        notifyDataSetChanged()
    }
}

//data관리
class DailyViewModel : ViewModel() {
    var tododata1 = MutableLiveData<List<Todo>>()
    private val data = arrayListOf<Todo>()

    fun addTodo(todo: Todo) {
        data.add(todo)
        tododata1.value = data
        Log.d("daily", "add: "+data.toString())
    }
    fun deleteTodo(todo: Todo) {
        data.remove(todo)
        tododata1.value = data
        Log.d("daily", "delete: "+data.toString())
    }
}

