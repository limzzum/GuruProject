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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.daily_add_popup.*
import java.util.ArrayList


/* 페이지 6번 - 날짜별 activity*/
private const val NUM_PAGES = 3
//private var data: ArrayList<Todo> = ArrayList(0)

class DailyActivity : AppCompatActivity() {
    private lateinit var mPager: ViewPager
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private val viewModel: DailyViewModel by viewModels()
    private var diolgcategory :String = ""
    private var diolgcontent : String = ""
    private var month:Int = 0;
    private var date:Int = 0;

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
    private fun showSettingPopup() {
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
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                when(position) {
                    0   ->  {
                        diolgcategory="토지"
                    }
                    1   ->  {
                        diolgcategory="물"
                    }
                    2   ->  {
                        diolgcategory="공기"
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
                viewModel.addTodo(Todo(month,date,diolgcategory,diolgcontent))
                Log.d("daily", diolgcategory)
                Log.d("daily", diolgcontent)
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
        , var month: Int, var date: Int
    ) :
        PagerAdapter() {
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            when (position) {
                0 -> {
                    val view =
                        layoutInflater.inflate(R.layout.activity_daliy_fragment1, container, false)
                    val textView1 = view.findViewById<TextView>(R.id.daily_date1)
                    if (date == 1) {
                        month-=1
                        if (month % 2 == 0) {
                            textView1.setText(
                                "${month}${resources.getString(R.string.month)} 30${resources.getString(
                                    R.string.date
                                )}"
                            )
                        } else {
                            textView1.setText(
                                "${month}${resources.getString(R.string.month)} 31${resources.getString(
                                    R.string.date
                                )}"
                            )
                        }
                    } else {
                        date-=1
                        textView1.setText(
                            "${month}${resources.getString(R.string.month)} ${date}${resources.getString(
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
                    viewModel.todoLivedata.observe(this@DailyActivity, Observer {
                        (recy1.adapter as DailyAdapter).setData(it)
                    })

                    // button
                    var add_btn1 = view.findViewById<Button>(R.id.daily_add1)
                    var ok_btn1 = view.findViewById<Button>(R.id.daily_ok1)
                    add_btn1.setOnClickListener {
                        showSettingPopup()
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
                        "${month}${resources.getString(R.string.month)} ${date}${resources.getString(
                            R.string.date
                        )}"
                    )
                    var add_btn2 = view.findViewById<Button>(R.id.daily_add2)
                    var ok_btn2 = view.findViewById<Button>(R.id.daily_ok2)
                    add_btn2.setOnClickListener {
                        showSettingPopup()
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
                    if (month % 2 == 0) {
                        if (date == 31) {
                            month+=1
                            textView3.setText(
                                "${month}${resources.getString(R.string.month)} 1${resources.getString(
                                    R.string.date
                                )}"
                            )
                        } else {
                            date+=1
                            textView3.setText(
                                "${month}${resources.getString(R.string.month)} ${date}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        }
                    } else {
                        if (date == 30) {
                            month+=1
                            textView3.setText(
                                "${month}${resources.getString(R.string.month)} 1${resources.getString(
                                    R.string.date
                                )}"
                            )
                        } else {
                            date+=1
                            textView3.setText(
                                "${month}${resources.getString(R.string.month)} ${date}${resources.getString(
                                    R.string.date
                                )}"
                            )
                        }
                    }

                    var add_btn3 = view.findViewById<Button>(R.id.daily_add3)
                    var ok_btn3 = view.findViewById<Button>(R.id.daily_ok3)
                    add_btn3.setOnClickListener {
                        showSettingPopup()
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

        val view1 = holder.view.findViewById<TextView>(R.id.daily_recy_text)
        view1.text = myDataset[position].category
        //view.
        val deleteButton = holder.view.findViewById<ImageButton>(R.id.daily_recy_delete)
        deleteButton.setOnClickListener {
            onClickDeleteIcon.invoke(todo) //onclickdeletIcon을 실행함
        }
    }

    override fun getItemCount() = myDataset.size

    fun setData(newData: List<Todo>){
        myDataset=newData
        notifyDataSetChanged()
    }
}

//data관리
class DailyViewModel : ViewModel() {
    var todoLivedata = MutableLiveData<List<Todo>>()
    private val data = arrayListOf<Todo>()

    fun addTodo(todo: Todo) {
        data.add(todo)
        todoLivedata.value=data
        Log.d("daily", data.toString())
    }

    fun deleteTodo(todo: Todo) {
        data.remove(todo)
        todoLivedata.value=data
    }
}

