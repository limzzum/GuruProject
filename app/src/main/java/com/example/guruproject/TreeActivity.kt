package com.example.guruproject

import android.graphics.Paint
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guruproject.databinding.ActivityTreeBinding
import com.example.guruproject.databinding.ItemMissionBinding
import kotlinx.android.synthetic.main.activity_tree.*
import java.util.*

private lateinit var binding: ActivityTreeBinding
data class iconData(var water:Int, var soil:Int, var air:Int, var tree:Int)
private var icon = iconData(4,4,4,0)
private var saveicon = iconData(0,0,0,0)

class TreeActivity : AppCompatActivity() {
    private val viewModel: TreeViewModel by viewModels()
    private var background_num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.addMission()
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

        // 해, 거름, 물뿌리개, 나무의 개수 표시
        air_num.setText("X${icon.air}")
        soil_num.setText("X${icon.soil}")
        water_num.setText("X${icon.water}" )
        tree_num.setText("X${icon.tree}")

        // 거름 그림을 클릭했을 때 전체적인 화면이 바뀌고, 거름의 수 -1
        soil_img.setOnClickListener {
            if ((icon.soil > 0) and (saveicon.soil!=100)) {
                icon.soil--
                background_num++
                changeBackground()
                saveicon.soil+=25
                binding.barsoil.setProgress(saveicon.soil)
            }
            soil_num.text = "X" + icon.soil.toString()
            viewModel.treeadd(saveicon)
        }

        // 물뿌리개 그림을 클릭했을 때 전체적인 화면이 바뀌고, 물뿌리개의 수 -1
        water_img.setOnClickListener {
            if ((icon.water > 0) and (saveicon.water!=100)) {
                icon.water--
                background_num++
                changeBackground()
                saveicon.water+=25
                binding.barwater.setProgress(saveicon.water)
            }
            water_num.text = "X" + icon.water.toString()
            viewModel.treeadd(saveicon)
        }

        // 해 그림을 클릭했을 때 전체적인 화면이 바뀌고, 해의 개수 -1
        air_img.setOnClickListener {
            if ((icon.air > 0) and (saveicon.air!=100)) {
                icon.air--
                background_num++
                changeBackground()
                saveicon.air+=25
                binding.barair.setProgress(saveicon.air)
            }
            air_num.text = "X" + icon.air.toString()
            viewModel.treeadd(saveicon)
        }

        // 관찰 UI 업데이트
        viewModel.missionLiveData.observe(this, androidx.lifecycle.Observer {
            (binding.missionRecyclerview.adapter as MissionAdapter).setData(it)
        })
    }

    // 화면 변경 함수
    private fun changeBackground() {
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
        } else{

        }
    }
}

data class Mission(val category:String, val text: String="", var isDone: Boolean = false)

// adapter 클래스
class MissionAdapter(
    private var myDataset: List<Mission>,
    val onClickItem: (mission: Mission) -> Unit
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
            Toast.makeText(it.context, "Clicked: ${mission.category}", Toast.LENGTH_LONG).show()
        }
        holder.binding.hideMissionCategory.text = mission.category
        holder.binding.hideMissionText.text = mission.text

        holder.binding.root.setOnClickListener {
            onClickItem.invoke(mission)
            if (mission.isDone) {
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
        }
    }

    override fun getItemCount() = myDataset.size

    // 데이터 갱신
    fun setData(newData: List<Mission>) {
        myDataset = newData
        notifyDataSetChanged()
    }
}

class TreeViewModel : ViewModel() {
    val missionLiveData = MutableLiveData<List<Mission>>()
    val iconLiveData = MutableLiveData<iconData>()
    val saveLiveDate= MutableLiveData<iconData>()
    private val data = arrayListOf<Mission>()

    // 미션 성공 실패의 여부를 부여하는 함수
    fun toggleMission(mission: Mission) {
        mission.isDone = !mission.isDone
        Log.d("tree", "water: "+icon.water)
        Log.d("tree", "mission: "+mission.category+", "+mission.isDone)
        if (mission.isDone == true) {
            when (mission.category) {
                "수질" -> {
                    binding.waterNum.setText("X${++icon.water}")
                    Log.d("tree","수질 안으로 들어옴")
                }
                "토양" -> {
                    binding.soilNum.setText("X${++icon.soil}")
                }
                "대기" -> {
                    binding.airNum.setText("X${++icon.air}")
                }
            }
        } else {
            when (mission.category) {
                "수질" -> {
                    binding.waterNum.setText("X${--icon.water}")
                }
                "토양" -> {
                    binding.soilNum.setText("X${--icon.soil}")
                }
                "대기" -> {
                    binding.airNum.setText("X${--icon.air}")
                }
            }
        }
        Log.d("tree", "water later: "+icon.water)
        iconLiveData.value = icon
    }

    // 요일에 따라 mission 변경 함수
    fun addMission() {
        val instance = Calendar.getInstance()
        var date = instance.get(Calendar.DAY_OF_WEEK).toInt()
        var data1 = Mission("")
        var data2 = Mission("")
        var data3 = Mission("")
        if (date == 1) {
            data1 = Mission("수질","양치컵 사용하기")
            data2 = Mission("토양", "길거리 쓰레기 줍기")
            data3 = Mission("대기","대중교통 이용하기")
            data.add(data1)
            data.add(data2)
            data.add(data3)
        } else if (date == 2) {
            data.clear()
            data1 = Mission("수질","텀블러 사용하기")
            data2 = Mission("토양","분리수거 하기")
            data3 = Mission("대기","자전거 이용하기")
            data.add(data1)
            data.add(data2)
            data.add(data3)
        } else if (date == 3) {
            data.clear()
            data1 = Mission("수질","텀블러 사용하기")
            data2 = Mission("토양","분리수거 하기")
            data3 = Mission("대기","자전거 이용하기")
            data.add(data1)
            data.add(data2)
            data.add(data3)
        } else if (date == 4) {
            data.clear()
            data1 = Mission("수질","양치컵 사용하기")
            data2 = Mission("토양", "길거리 쓰레기 줍기")
            data3 = Mission("대기","대중교통 이용하기")
            data.add(data1)
            data.add(data2)
            data.add(data3)
        } else if (date == 5) {
            data.clear()
            data1 = Mission("수질","텀블러 사용하기")
            data2 = Mission("토양","분리수거 하기")
            data3 = Mission("대기","자전거 이용하기")
            data.add(data1)
            data.add(data2)
            data.add(data3)
        } else if (date == 6) {
            data.clear()
            data1 = Mission("수질","텀블러 사용하기")
            data2 = Mission("토양","분리수거 하기")
            data3 = Mission("대기","자전거 이용하기")
            data.add(data1)
            data.add(data2)
            data.add(data3)
        } else if (date == 7) {
            data.clear()
            data1 = Mission("수질","텀블러 사용하기")
            data2 = Mission("토양","분리수거 하기")
            data3 = Mission("대기","자전거 이용하기")
            data.add(data1)
            data.add(data2)
            data.add(data3)
        }
        missionLiveData.value = data
    }

    fun treeadd(saveicon:iconData){
        if((saveicon.air==100)and(saveicon.soil==100)and(saveicon.water==100)){
            binding.treeNum.setText("X${++saveicon.tree}")
            Log.d("tree","savetree"+saveicon.tree)
            saveicon.soil=0
            saveicon.air=0
            saveicon.water=0
            binding.barair.setProgress(0)
            binding.barsoil.setProgress(0)
            binding.barwater.setProgress(0)
        }
        saveLiveDate.value=saveicon
    }
}

