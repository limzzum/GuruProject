package com.example.guruproject

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruproject.databinding.ActivityTreeBinding
import com.example.guruproject.databinding.ItemMissionBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_tree.*
import java.util.*


private lateinit var binding: ActivityTreeBinding

class TreeActivity : AppCompatActivity() {
    private val treeviewModel: TreeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        CustomClass(this)
        super.onCreate(savedInstanceState)
        binding = ActivityTreeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // 오늘의 미션 item을 누르면 togglemission함수 불러옴
        binding.missionRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@TreeActivity)
            adapter = MissionAdapter(
                emptyList(),
                onClickItem = {
                    treeviewModel.toggleMission(it)
                }
            )
        }

        // 오늘의 미션을 누르면 밑에 미션들이 나왔다가 안나왔다가 하는 함수
        today_misson.setOnClickListener {
            if (mission_recyclerview.visibility == View.VISIBLE) {
                mission_recyclerview.visibility = View.GONE
            } else {
                mission_recyclerview.visibility = View.VISIBLE
            }
        }

        // 해, 물뿌리개, 거름, 나무 그림을 클릭했을 때 전체적인 화면이 바뀌고, 해당 그림의 수 -1
        soil_img.setOnClickListener {
            treeviewModel.imgClick("soil", view)
        }
        water_img.setOnClickListener {
            treeviewModel.imgClick("water", view)
        }
        air_img.setOnClickListener {
            treeviewModel.imgClick("air", view)
        }
        tree_img.setOnClickListener {
            treeviewModel.treeclick()
        }

        // tab 연결
        val button1: ImageButton = findViewById(R.id.tree)
        val button2: ImageButton = findViewById(R.id.calendar)
        val button3: ImageButton = findViewById(R.id.community)
        val button4: ImageButton = findViewById(R.id.my_info)

        button1.setOnClickListener {
            val intent = Intent(this@TreeActivity, TreeActivity::class.java)
            startActivity(intent)
        }
        button2.setOnClickListener {
            val intent = Intent(this@TreeActivity, CalendarActivity::class.java)
            startActivity(intent)
        }
        button3.setOnClickListener {
            val intent = Intent(this@TreeActivity, com.example.guruproject.RecyclerView::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener {
            val intent = Intent(this@TreeActivity, mypage::class.java)
            startActivity(intent)
        }

        // 관찰 UI 업데이트
        treeviewModel.missionLiveData.observe(this, androidx.lifecycle.Observer {
            (binding.missionRecyclerview.adapter as MissionAdapter).setData(it)
        })
        treeviewModel.iconLiveData.observe(this, androidx.lifecycle.Observer {
            binding.airNum.setText("X${it.getLong("air")}")
            binding.waterNum.setText("X${it.getLong("water")}")
            binding.soilNum.setText("X${it.getLong("soil")}")
            binding.treeNum.setText("X${it.getLong("tree")}")
        })
        treeviewModel.saveLiveDate.observe(this, androidx.lifecycle.Observer {
            binding.barsoil.setProgress(it.getLong("soil")!!.toInt())
            binding.barair.setProgress(it.getLong("air")!!.toInt())
            binding.barwater.setProgress(it.getLong("water")!!.toInt())
        })
    }

    private var backPressedTime: Long = 0
    override fun onBackPressed() {
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            ActivityCompat.finishAffinity(this) //앱 종료
            return
        }
        // 처음 클릭 메시지
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}

// adapter 클래스
class MissionAdapter(
    private var myDataset: List<DocumentSnapshot>,
    val onClickItem: (mission: DocumentSnapshot) -> Boolean
) :
    RecyclerView.Adapter<MissionAdapter.MissionViewHolder>() {

    class MissionViewHolder(val binding: ItemMissionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MissionAdapter.MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mission, parent, false)
        return MissionViewHolder(ItemMissionBinding.bind(view))
    }

    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val mission = myDataset[position]

        when (mission.getString("category")) {
            "air" -> {
                holder.binding.hideMissionCategory.text =
                    mContext!!.getResources().getString(R.string.air)
            }
            "soil" -> {
                holder.binding.hideMissionCategory.text =
                    mContext!!.getResources().getString(R.string.soil)
            }
            "water" -> {
                holder.binding.hideMissionCategory.text =
                    mContext!!.getResources().getString(R.string.water)
            }
            else -> {
            }
        }
        holder.binding.hideMissionText.text = mission.getString("text")

        // 미션 글씨 바꾸기
        if ((mission.getBoolean("isDone") ?: false) == true) {
            holder.binding.hideMissionText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.NORMAL)
            }
        } else {
            holder.binding.hideMissionText.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
            }
        }

        holder.binding.root.setOnClickListener {
            val bol: Boolean = onClickItem.invoke(mission)
        }
    }

    override fun getItemCount() = myDataset.size

    // 데이터 갱신
    fun setData(newData: List<DocumentSnapshot>) {
        myDataset = newData
        notifyDataSetChanged()
    }
}

class TreeViewModel : ViewModel() {
    val db = Firebase.firestore
    val missionLiveData = MutableLiveData<List<DocumentSnapshot>>()
    val iconLiveData = MutableLiveData<DocumentSnapshot>()
    val saveLiveDate = MutableLiveData<DocumentSnapshot>()
    val tododata1 = MutableLiveData<List<DocumentSnapshot>>()

    init {
        fetchData()
    }

    fun fetchData() {
        val user = auth.currentUser
        if (user != null) {
            db.collection(user.uid)
                .document("treesave")
                .addSnapshotListener { value, e ->
                    if (e != null) { //에러가 나면
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        saveLiveDate.value = value
                    }
                    changeBackground(saveLiveDate.value!!.getLong("air")!!.toInt(),
                        saveLiveDate.value!!.getLong("soil")!!.toInt(),
                        saveLiveDate.value!!.getLong("water")!!.toInt())
                }
            db.collection(user.uid)
                .document("treeicon")
                .addSnapshotListener { value, e ->
                    if (e != null) { //에러가 나면
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        iconLiveData.value = value
                    }
                }
            db.collection(user.uid)
                .document(user.uid)
                .collection("treemission")
                .addSnapshotListener { value, e ->
                    if (e != null) { //에러가 나면
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        missionLiveData.value = value.documents
                    }
                    switchdate()
                }
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
                }
        }
    }

    // 날짜가 바뀌면 isDone을 다 false로, 요일별로 미션 다르게, 현재 날짜로 날짜 데이터 바꾸기
    fun switchdate() {
        var airmissions = mContext!!.getResources().getStringArray(R.array.airmission);
        var soilmissions = mContext!!.getResources().getStringArray(R.array.soilmission);
        var watermissions = mContext!!.getResources().getStringArray(R.array.watermission);
        var airnum = Random().nextInt(airmissions.size)
        var soilnum = Random().nextInt(soilmissions.size)
        var waternum = Random().nextInt(watermissions.size)

        val instance = Calendar.getInstance()
        val date = instance.get(Calendar.DATE)
        if (date != missionLiveData.value!!.get(0).getLong("date")!!.toInt()) {
            auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("air").update("date", date)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("soil").update("date", date)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("water").update("date", date)

                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("air").update("text", airmissions.get(airnum))
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("soil").update("text", soilmissions.get(soilnum))
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("water").update("text", watermissions.get(waternum))

                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("air").update("isDone", false)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("soil").update("isDone", false)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("water").update("isDone", false)
            }
        }
    }

    // 미션 성공 실패의 여부를 부여하는 함수
    fun toggleMission(mission: DocumentSnapshot): Boolean {
        var isDone: Boolean = mission.getBoolean("isDone")!!
        isDone = !isDone
        var value: Int = -1
        val instance = Calendar.getInstance()

        if (isDone == true) {
            value = iconLiveData.value!!.getLong(mission.getString("category")!!)!!.toInt() + 1

            var todo: Todo = Todo(0, 0, 0, "", "")
            when (mission.getString("category")!!) {
                "air" -> {
                    todo = Todo(
                        instance.get(Calendar.YEAR),
                        instance.get(Calendar.MONTH) + 1,
                        instance.get(Calendar.DATE),
                        "공기mission",
                        missionLiveData.value!!.get(0).getString("text")!!
                    )
                }
                "soil" -> {
                    todo = Todo(
                        instance.get(Calendar.YEAR),
                        instance.get(Calendar.MONTH) + 1,
                        instance.get(Calendar.DATE),
                        "토지mission",
                        missionLiveData.value!!.get(1).getString("text")!!
                    )
                }
                "water" -> {
                    todo = Todo(
                        instance.get(Calendar.YEAR),
                        instance.get(Calendar.MONTH) + 1,
                        instance.get(Calendar.DATE),
                        "물mission",
                        missionLiveData.value!!.get(2).getString("text")!!
                    )
                }
            }
            auth.currentUser?.let { user ->
                db.collection(user.uid).document(user.uid).collection("todo").add(todo)
            }
        } else {
            value = iconLiveData.value!!.getLong(mission.getString("category")!!)!!.toInt() - 1
            var size = tododata1.value!!.size - 1
            for (i in 0..size) {
                var item = tododata1.value!!.get(i)
                if (((item.getLong("year"))!!.toInt() == instance.get(Calendar.YEAR))
                    and ((item.getLong("month"))!!.toInt() == instance.get(Calendar.MONTH) + 1)
                    and ((item.getLong("date"))!!.toInt() == instance.get(Calendar.DATE))
                    and (item.getString("category")!!.contains("mission"))
                    and (item.getString("content") == mission.getString("text"))
                ) {
                    auth.currentUser?.let { user ->
                        db.collection(user.uid).document(user.uid).collection("todo")
                            .document(item.id).delete()
                    }
                    break
                }
            }
        }
        auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
            db.collection(user.uid).document(user.uid).collection("treemission")
                .document(mission.getString("category")!!).update("isDone", isDone)
            db.collection(user.uid).document("treeicon")
                .update(mission.getString("category")!!, value)
        }
        return isDone
    }

    // tree 이미지 누르면 나무 한그루 더해짐
    fun treeadd() {
        var saveair = saveLiveDate.value!!.getLong("air")!!.toInt()
        var savesoil = saveLiveDate.value!!.getLong("soil")!!.toInt()
        var savewater = saveLiveDate.value!!.getLong("water")!!.toInt()
        var icontree = iconLiveData.value!!.getLong("tree")!!.toInt()
        if ((saveair == 100) and (savesoil == 100) and (savewater == 100)) {
            icontree++
            saveair=0
            savesoil=0
            savewater=0
            auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
                db.collection(user.uid).document("treeicon").update("tree", icontree)
                db.collection(user.uid).document("treesave").update("air", saveair)
                db.collection(user.uid).document("treesave").update("soil", savesoil)
                db.collection(user.uid).document("treesave").update("water", savewater)
            }
        }
        changeBackground(saveair, savesoil, savewater)
    }

    // 공기, 물, 토지 이미지 누르면 값-1
    fun iconmius(id: String, value: Int) {
        auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
            db.collection(user.uid).document("treeicon").update(id, value)
        }
    }

    // 공기, 물, 토지 이미지 누르면 프로그래스 바 값 변경
    fun saveplus(id: String, value: Int) {
        auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
            db.collection(user.uid).document("treesave").update(id, value)
        }
    }

    // 이미지 누르면
    fun imgClick(id: String, view: View) {
        var savedata = saveLiveDate.value!!.getLong(id)!!.toInt()
        if ((iconLiveData.value!!.getLong(id)!! > 0) and (savedata != 100)) {
            iconmius(id, iconLiveData.value!!.getLong(id)!!.toInt() - 1)
            savedata += 25
            saveplus(id, savedata)
        }
        var saveair = saveLiveDate.value!!.getLong("air")!!.toInt()
        var savesoil = saveLiveDate.value!!.getLong("soil")!!.toInt()
        var savewater = saveLiveDate.value!!.getLong("water")!!.toInt()
        if ((saveair == 100) and (savesoil == 100) and (savewater == 100)) {
            Toast.makeText(view.context, "나무를 눌러주세요!", Toast.LENGTH_LONG).show()
        }
        changeBackground(saveair, savesoil, savewater)
    }

    // tree이미지 누르면
    fun treeclick() {
        treeadd()
    }
}

// 바탕화면(나무사진) 변경 함수
fun changeBackground(saveair: Int, savesoil:Int, savewater:Int) {
    if ((saveair>=100) and (savesoil>=100) and (savewater>=100)) { //마지막
        binding.treeImageView2.setImageResource(R.drawable.tree5)
    }else if ((saveair>=75) and (savesoil>=75) and (savewater>=75)) {
        binding.treeImageView2.setImageResource(R.drawable.tree1)
    } else if ((saveair>=50) and (savesoil>=50) and (savewater>=50)) {
        binding.treeImageView2.setImageResource(R.drawable.tree2)
    } else if ((saveair>=25) and (savesoil>=25) and (savewater>=25)) {
        binding.treeImageView2.setImageResource(R.drawable.tree3)
    } else {
        binding.treeImageView2.setImageResource(R.drawable.tree4)
    }
}

// activity 외 class에서 context 사용하기 위해
var mContext: Context? = null
fun CustomClass(context: Context?) {
    mContext = context
}