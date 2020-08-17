package com.example.guruproject

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.example.guruproject.R
import com.example.guruproject.databinding.ActivityMainBinding
import com.example.guruproject.databinding.ActivityTreeBinding
import com.example.guruproject.databinding.ItemMissionBinding
import kotlinx.android.synthetic.main.item_mission.*
import kotlinx.android.synthetic.main.activity_tree.*
//import org.mozilla.javascript.tools.jsc.Main
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

private var air_number = 2
private var soil_number = 2
private var water_number = 2

class TreeActivity : AppCompatActivity() {

    private val data = arrayListOf<Mission>()

    private lateinit var binding: ActivityTreeBinding

    private var background_num = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        timeMission()

        super.onCreate(savedInstanceState)
        binding = ActivityTreeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        // 오늘의 미션 item을 누르면 togglemission함수 불러옴
        binding.missionRecyclerview.apply {
            layoutManager = LinearLayoutManager(this@TreeActivity)
            adapter = MissionAdapter(
                data,
                onClickItem = {
                    toggleMission(it)
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
        air_num.setText("X$air_number")
        soil_num.setText("X$soil_number")
        water_num.setText("X$water_number")
        var tree_number = 0
        tree_num.setText("X$tree_number")

        // 거름 그림을 클릭했을 때 전체적인 화면이 바뀌고, 거름의 수 -1
        soil_img.setOnClickListener {
            if (soil_number > 0) {
                soil_number--
                background_num++
                changeBackground()
                soil_num.text = "X" + soil_number.toString()
            } else {
                soil_num.text = "X" + soil_number.toString()
            }
        }

        // 물뿌리개 그림을 클릭했을 때 전체적인 화면이 바뀌고, 물뿌리개의 수 -1
        water_img.setOnClickListener {
            if (water_number > 0) {
                water_number--
                background_num++
                changeBackground()
                water_num.text = "X" + water_number.toString()
            } else {
                water_num.text = "X" + water_number.toString()
            }
        }

        // 해 그림을 클릭했을 때 전체적인 화면이 바뀌고, 해의 개수 -1
        air_img.setOnClickListener {
            if (air_number > 0) {
                air_number--
                background_num++
                changeBackground()
                air_num.text = "X" + air_number.toString()
            } else {
                air_num.text = "X" + air_number.toString()
            }
        }
    }

    // 미션 성공 실패의 여부를 부여하는 함수
    private fun toggleMission(mission: Mission) {
        if (mission.isDone == false) {
            mission.isDone = !mission.isDone
            binding.missionRecyclerview.adapter?.notifyDataSetChanged()
        }
    }

    private fun timeMission() {
        val instance = Calendar.getInstance()
        var date = instance.get(Calendar.DAY_OF_WEEK).toInt()
        if (date == 1) {
            data.add(Mission("수질 : 양치컵 사용하기"))
            data.add(Mission("토양 : 길거리 쓰레기 줍기"))
            data.add(Mission("대기 : 대중교통 이용하기"))
        } else if (date == 2) {
            data.clear()
            data.add(Mission("수질 : 텀블러 사용하기"))
            data.add(Mission("토양 : 분리수거 하기"))
            data.add(Mission("대기 : 자전거 이용하기"))
        } else if (date == 3) {
            data.clear()
            data.add(Mission("수질 : "))
            data.add(Mission("토양 : "))
            data.add(Mission("대기 : "))
        } else if (date == 4) {
            data.clear()
            data.add(Mission("수질 : "))
            data.add(Mission("토양 : "))
            data.add(Mission("대기 : "))
        } else if (date == 5) {
            data.clear()
            data.add(Mission("수질 : "))
            data.add(Mission("토양 : "))
            data.add(Mission("대기 : "))
        } else if (date == 6) {
            data.clear()
            data.add(Mission("수질 : "))
            data.add(Mission("토양 : "))
            data.add(Mission("대기 : "))
        } else if (date == 7) {
            data.clear()
            data.add(Mission("수질 : "))
            data.add(Mission("토양 : "))
            data.add(Mission("대기 : "))
        }
    }

    private fun clickMission(mission: Mission, position: Int) {
        if (position == 1) {
            air_number++
        } else if (position == 2) {
            soil_number++
        } else if (position == 3) {
            water_number++
        }
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
        }
    }


}

data class Mission(val text: String, var isDone: Boolean = false)

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
        holder.binding.hideMission.text = mission.text
        if (mission.isDone) {
            holder.binding.hideMission.apply {
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                setTypeface(null, Typeface.NORMAL)
            }
        } else {
            holder.binding.hideMission.apply {
                paintFlags = 0
                setTypeface(null, Typeface.BOLD)
            }
            holder.binding.root.setOnClickListener {
                onClickItem.invoke(mission)
            }
        }
    }

    override fun getItemCount() = myDataset.size
}
