package com.example.guruproject

import android.content.Intent
import android.content.res.Resources
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import kotlin.collections.ArrayList


private lateinit var binding: ActivityTreeBinding
val cal = Calendar.getInstance()
private var background_num = 0

class TreeActivity : AppCompatActivity() {
    private val viewModel: TreeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreeBinding.inflate(layoutInflater)
        val view = binding.root
        cal.time = Date()
        setContentView(view)

        // 오늘의 미션 item을 누르면 togglemission함수 불러옴
        binding.missionRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@TreeActivity)
            adapter = MissionAdapter(
                emptyList(),
                onClickItem = {
                    viewModel.toggleMission(it)
                })
        }

        // 오늘의 미션을 누르면 밑에 미션들이 나왔다가 안나왔다가 하는 함수
        today_misson.setOnClickListener {
            if (mission_recyclerview.visibility == View.VISIBLE) {
                mission_recyclerview.visibility = View.GONE
            } else {
                mission_recyclerview.visibility = View.VISIBLE
            }
        }

        // 거름 그림을 클릭했을 때 전체적인 화면이 바뀌고, 거름의 수 -1
        soil_img.setOnClickListener {
            viewModel.imgClick("soil", view)
        }
        // 물뿌리개 그림을 클릭했을 때 전체적인 화면이 바뀌고, 물뿌리개의 수 -1
        water_img.setOnClickListener {
            viewModel.imgClick("water", view)
        }
        // 해 그림을 클릭했을 때 전체적인 화면이 바뀌고, 해의 개수 -1
        air_img.setOnClickListener {
            viewModel.imgClick("air", view)
        }
        tree_img.setOnClickListener {
            viewModel.treeclick()
        }

        // tab 연결
        all_list.setOnClickListener{
            val intent1 = Intent(this@TreeActivity, TreeActivity::class.java)
            startActivity(intent1)
            finish()//이 activity 종료
        }
        my_list.setOnClickListener {
            val intent1 = Intent(this@TreeActivity, CalendarActivity::class.java)
            startActivity(intent1)
        }
        upload.setOnClickListener {
            val intent1 = Intent(this@TreeActivity, com.example.guruproject.RecyclerView::class.java)
            startActivity(intent1)
        }
        user_info.setOnClickListener {
//            val intent1 = Intent(this@TreeActivity, mypage::class.java)
//            startActivity(intent1)
        }

        // 관찰 UI 업데이트
        viewModel.missionLiveData.observe(this, androidx.lifecycle.Observer {
            (binding.missionRecyclerview.adapter as MissionAdapter).setData(it)
        })
        viewModel.iconLiveData.observe(this, androidx.lifecycle.Observer {
            binding.airNum.setText("X${it.getLong("air")}")
            binding.waterNum.setText("X${it.getLong("water")}")
            binding.soilNum.setText("X${it.getLong("soil")}")
            binding.treeNum.setText("X${it.getLong("tree")}")
        })
        viewModel.saveLiveDate.observe(this, androidx.lifecycle.Observer {
            binding.barsoil.setProgress(it.getLong("soil")!!.toInt())
            binding.barair.setProgress(it.getLong("air")!!.toInt())
            binding.barwater.setProgress(it.getLong("water")!!.toInt())
        })
    }

    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            ActivityCompat.finishAffinity(this)
            finish()
            return
        }
        // 처음 클릭 메시지
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}

// 화면 변경 함수
fun changeBackground() {
    if (background_num == 1) {
        binding.treeRelativelayout.setBackgroundResource(R.drawable.tree1)
    } else if (background_num == 2) {
        binding.treeRelativelayout.setBackgroundResource(R.drawable.tree2)
    } else if (background_num == 3) {
        binding.treeRelativelayout.setBackgroundResource(R.drawable.tree3)
    } else if (background_num == 4) {
        binding.treeRelativelayout.setBackgroundResource(R.drawable.tree4)
    } else if (background_num == 5) {
        binding.treeRelativelayout.setBackgroundResource(R.drawable.tree5)
    } else if (background_num == 6) {
        binding.treeRelativelayout.setBackgroundResource(R.drawable.tree6)
    } else if (background_num == 7) {
        binding.treeRelativelayout.setBackgroundResource(R.drawable.tree7)
    } else {

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
        val listener = View.OnClickListener { it ->
            Toast.makeText(it.context, "Clicked: ${mission.get("category")}", Toast.LENGTH_LONG)
                .show()
        }
        when (mission.getString("category")) {
            "air" -> {
                holder.binding.hideMissionCategory.text = "공기"
            }
            "soil" -> {
                holder.binding.hideMissionCategory.text = "토지"
            }
            "water" -> {
                holder.binding.hideMissionCategory.text = "물"
            }
            else -> {
            }
        }
        holder.binding.hideMissionText.text = mission.getString("text")

        if ((mission.getBoolean("isDone") ?: false) == true) { //todo의 isDone이 false이면
            holder.binding.hideMissionText.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.NORMAL)
                Log.d("click: ", "true")
            }
        } else {
            holder.binding.hideMissionText.apply {
                paintFlags = 0
                setTypeface(null, Typeface.NORMAL)
                Log.d("click: ", "false")
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
    val missionLiveData = MutableLiveData<List<DocumentSnapshot>>()//List<Mission>
    val iconLiveData = MutableLiveData<DocumentSnapshot>()//iconData
    val saveLiveDate = MutableLiveData<DocumentSnapshot>()//iconData
    private val data = arrayListOf<DocumentSnapshot>()

    init {
        fetchData()
    }

    fun fetchData() {
        val user = auth.currentUser //FirebaseAuth.getInstance()
        if (user != null) {
            db.collection(user.uid)
                .document("treesave")
                .addSnapshotListener { value, e ->
                    if (e != null) { //에러가 나면
                        Log.d("togg", "err: " + e)
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        saveLiveDate.value = value
                        Log.d("togg", "savedata: " + value)
                    }
                }
            db.collection(user.uid)
                .document("treeicon")
                .addSnapshotListener { value, e ->
                    if (e != null) { //에러가 나면
                        Log.d("togg", "err: " + e)
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        iconLiveData.value = value
                        Log.d("tree", "icondata: " + value)
                    }
                }
            db.collection(user.uid)
                .document(user.uid)
                .collection("treemission")
                .addSnapshotListener { value, e ->
                    if (e != null) { //에러가 나면
                        Log.d("togg", "err: " + e)
                        return@addSnapshotListener
                    }
                    if (value != null) {
                        missionLiveData.value = value.documents
                        Log.d("tree", "missiondata: " + value)
                    }
                    switchdate()
                    addMission()
                }
        }
    }

    // 날짜가 바뀌면 isDone을 다 false로
    fun switchdate() {
        val instance = Calendar.getInstance()
        val date = instance.get(Calendar.DATE).toInt()
        Log.d("date: ", "curdate: " + date)
        Log.d("date: ", "datadate: " + missionLiveData.value!!.get(0).getLong("date")!!.toInt())
        if (date != missionLiveData.value!!.get(0).getLong("date")!!.toInt()) {
            auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("air").update("isDone", false)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("soil").update("isDone", false)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("water").update("isDone", false)

                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("air").update("date", date)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("soil").update("date", date)
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("water").update("date", date)
            }
        }
    }

    // 미션 성공 실패의 여부를 부여하는 함수
    fun toggleMission(mission: DocumentSnapshot): Boolean {
        var isDone: Boolean = mission.getBoolean("isDone")!!
        isDone = !isDone
        var value: Int = -1
        if (isDone == true) {
            value = iconLiveData.value!!.getLong(mission.getString("category")!!)!!.toInt() + 1
        } else {
            value = iconLiveData.value!!.getLong(mission.getString("category")!!)!!.toInt() - 1
        }
        auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
            db.collection(user.uid).document(user.uid).collection("treemission")
                .document(mission.getString("category")!!).update("isDone", isDone)
            db.collection(user.uid).document("treeicon")
                .update(mission.getString("category")!!, value)
        }
        return isDone
    }

    // 요일에 따라 mission 변경 함수
    fun addMission() {
        var airnum = Random().nextInt(5)
        var soilnum = Random().nextInt(5)
        var waternum = Random().nextInt(3)
        Log.d("random", "" + airnum + soilnum + waternum)

        var airmissions = ArrayList<String>()
        var soilmissions = ArrayList<String>()
        var watermissions = ArrayList<String>()

        airmissions.add("대중교통 이용하기")
        airmissions.add("에어컨 대신 선풍기 사용하기")
        airmissions.add("도보/자전거 이용하기")
        airmissions.add("저탄소 인증 제품 사용하기")
        airmissions.add("식물에 물 주기")


        soilmissions.add("장바구니 사용하기")
        soilmissions.add("분리수거하기")
        soilmissions.add("인쇄물 대신 전자문서 사용하기")
        soilmissions.add("이면지 사용하기")
        soilmissions.add("일회용품 사용하지 않기")

        watermissions.add("세제 조금만 사용하기")
        watermissions.add("양치 컵 사용하기")
        watermissions.add("불필요할 때 물 잠그기")

        val instance = Calendar.getInstance()
        val date = instance.get(Calendar.DATE).toInt()
        if (date != missionLiveData.value!!.get(0).getLong("date")!!.toInt()) {
            auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("air").update("text", airmissions.get(airnum))
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("soil").update("text", soilmissions.get(soilnum))
                db.collection(user.uid).document(user.uid).collection("treemission")
                    .document("water").update("text", watermissions.get(waternum))
            }
        }
    }

    // tree 이미지 누르면 나무 한그루 더해짐
    fun treeadd() {
        Log.d("add: ", "treeadd into")
        var saveair = saveLiveDate.value!!.getLong("air")!!.toInt()
        var savesoil = saveLiveDate.value!!.getLong("soil")!!.toInt()
        var savewater = saveLiveDate.value!!.getLong("water")!!.toInt()
        Log.d("add: ", "saveair: " + saveair)
        Log.d("add: ", "savesoil: " + savesoil)
        Log.d("add: ", "savewater: " + savewater)
        var icontree = iconLiveData.value!!.getLong("tree")!!.toInt()
        if ((saveair == 100) and (savesoil == 100) and (savewater == 100)) {
            icontree++
            auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
                db.collection(user.uid).document("treeicon").update("tree", icontree)
                db.collection(user.uid).document("treesave").update("air", 0)
                db.collection(user.uid).document("treesave").update("soil", 0)
                db.collection(user.uid).document("treesave").update("water", 0)
            }
        }
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
            background_num++
            changeBackground()
            savedata += 25
            saveplus(id, savedata)
        }
        var saveair = saveLiveDate.value!!.getLong("air")!!.toInt()
        var savesoil = saveLiveDate.value!!.getLong("soil")!!.toInt()
        var savewater = saveLiveDate.value!!.getLong("water")!!.toInt()
        if ((saveair == 100) and (savesoil == 100) and (savewater == 100)) {
            Toast.makeText(view.context, "나무를 눌러주세요!", Toast.LENGTH_LONG).show()
        }
    }

    // tree이미지 누르면
    fun treeclick() {
        treeadd()
    }
}

