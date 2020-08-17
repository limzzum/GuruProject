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


/* 페이지 6번 - 날짜별 activity*/
private const val NUM_PAGES = 3

class DailyActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager
    private val viewModel: DailyViewModel by viewModels()
    private var diolgcategory: String = ""
    private var diolgcontent: String = ""
    private var month: Int = 0;
    private var date: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        // pager
        mPager = findViewById(R.id.view_pager)
        month = intent.getIntExtra("month", 0)
        date = intent.getIntExtra("date", 0)
        val pagerAdapter = ScreenSlidePagerAdapter(
            LayoutInflater.from(this@DailyActivity),
            month,
            date
        )
        mPager.adapter = pagerAdapter
        mPager.setCurrentItem(1, true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    //팝업
    private fun showSettingPopup(pagemonth: Int, pagedate: Int) {
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
                //Toast.makeText(applicationContext, "저장되었습니다", Toast.LENGTH_LONG)
                diolgcontent = daily_pop_content.text.toString()
                viewModel.addTodo(Todo(pagemonth, pagedate, diolgcategory, diolgcontent))
                Log.d("daily", "pop " + pagemonth)
                Log.d("daily", "pop " + pagedate)
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
        , var curmonth: Int, var curdate: Int
    ) :
        PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            when (position) {
                0 -> {
                    var pagemonth1 = curmonth
                    var pagedate1 = curdate
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment1, container, false)
                    val textView1 = view.findViewById<TextView>(R.id.daily_date1)
                    if (curdate == 1) {
                        pagemonth1 = curmonth - 1
                        if (curmonth % 2 == 0) {
                            pagedate1 = 30
                            textView1.setText(
                                "${pagemonth1}${resources.getString(R.string.month)} ${pagedate1}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        } else {
                            pagedate1 = 31
                            textView1.setText(
                                "${pagemonth1}${resources.getString(R.string.month)} ${pagedate1}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        }
                    } else {
                        pagedate1 = curdate - 1
                        textView1.setText(
                            "${pagemonth1}${resources.getString(R.string.month)} ${pagedate1}${resources.getString(
                                R.string.date
                            )}"
                        )
                    }
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
                        Log.d("daily", "" + pagemonth1 + "  /  " + pagedate1)
                        for (item in it) {
                            if ((item.month == pagemonth1) and (item.date == pagedate1)) {

                                curList.add(item)
                                (recy1.adapter as DailyAdapter).setData(curList)
                            }
                        }
                        Log.d("daily", "item" + curList.toString())
                    })

                    // button
                    var add_btn1 = view.findViewById<Button>(R.id.daily_add1)
                    var ok_btn1 = view.findViewById<Button>(R.id.daily_ok1)
                    add_btn1.setOnClickListener {
                        showSettingPopup(pagemonth1, pagedate1)
                    }
                    ok_btn1.setOnClickListener {
                        finish()
                    }
                    container.addView(view)
                    return view
                }
                1 -> {
                    var pagemonth2 = curmonth
                    var pagedate2 = curdate
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment2, container, false)
                    val textView2 = view.findViewById<TextView>(R.id.daily_date2)
                    textView2.setText(
                        "${pagemonth2}${resources.getString(R.string.month)} ${pagedate2}${resources.getString(
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

                        (recy2.adapter as DailyAdapter).setData(it)
                    })

                    var add_btn2 = view.findViewById<Button>(R.id.daily_add2)
                    var ok_btn2 = view.findViewById<Button>(R.id.daily_ok2)
                    add_btn2.setOnClickListener {
                        showSettingPopup(pagemonth2, pagedate2)
                    }
                    ok_btn2.setOnClickListener {
                        finish()
                    }
                    container.addView(view)
                    return view
                }
                2 -> {
                    var pagemonth3 = curmonth
                    var pagedate3 = curdate
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment3, container, false)
                    val textView3 = view.findViewById<TextView>(R.id.daily_date3)
                    if (curmonth % 2 == 0) {
                        if (curdate == 31) {
                            pagemonth3 = curmonth + 1
                            pagedate3 = 1
                            textView3.setText(
                                "${pagemonth3}${resources.getString(R.string.month)} ${pagedate3}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        } else {
                            pagedate3 = curdate + 1
                            textView3.setText(
                                "${pagemonth3}${resources.getString(R.string.month)} ${pagedate3}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        }
                    } else {
                        if (curdate == 30) {
                            pagemonth3 = curmonth + 1
                            pagedate3 = 1
                            textView3.setText(
                                "${pagemonth3}${resources.getString(R.string.month)} ${pagedate3}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        } else {
                            pagedate3 = curdate + 1
                            textView3.setText(
                                "${pagemonth3}${resources.getString(R.string.month)} ${pagedate3}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        }
                    }

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
                        (recy3.adapter as DailyAdapter).setData(it)
                    })

                    var add_btn3 = view.findViewById<Button>(R.id.daily_add3)
                    var ok_btn3 = view.findViewById<Button>(R.id.daily_ok3)
                    add_btn3.setOnClickListener {
                        showSettingPopup(pagemonth3, pagedate3)
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
        //view.
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

    //    var tododata2 = MutableLiveData<List<Todo>>()
//    var tododata3 = MutableLiveData<List<Todo>>()
    private val data = arrayListOf<Todo>()

    fun addTodo(todo: Todo) {
        data.add(todo)
        tododata1.value = data
        Log.d("daily", data.toString())
    }

    fun deleteTodo(todo: Todo) {
        data.remove(todo)
        tododata1.value = data
    }
}

