
package com.example.guruproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.primitives.UnsignedLongs.sort
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_loadview.*
import kotlinx.android.synthetic.main.activity_recycler_view.*
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort
import kotlin.collections.ArrayList


class RecyclerView : AppCompatActivity() {
//var TAG="데이터"
//    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val button1: ImageButton =findViewById(R.id.tree)
        val button2: ImageButton =findViewById(R.id.calendar)
        val button3: ImageButton =findViewById(R.id.community)
        val button4: ImageButton =findViewById(R.id.my_info)

        button1.setOnClickListener{
            val intent = Intent(this@RecyclerView,TreeActivity::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            val intent = Intent(this@RecyclerView, CalendarActivity::class.java)
            startActivity(intent)
        }


        button3.setOnClickListener {
            val intent = Intent(this@RecyclerView, com.example.guruproject.RecyclerView::class.java)
            startActivity(intent)
        }
        button4.setOnClickListener {
            val intent = Intent(this@RecyclerView, mypage::class.java)
            startActivity(intent)
        }
        upload_button.setOnClickListener {
            val intent = Intent(this, loadview::class.java)
            startActivity(intent)
        }

        var ProfileListDTO: ArrayList<Profiles> = ArrayList<Profiles>()
        var db = FirebaseFirestore.getInstance()


        db.collection("users").whereEqualTo("userId", "User")
            .get()
            .addOnSuccessListener { result ->

                var profileDTO: Profiles

                for (document in result) {
                    //Log.d(TAG,"$document.id}=>$document.data}")
                    profileDTO = document.toObject(Profiles::class.java)
                    ProfileListDTO.add(profileDTO)
                    //db.collection("user").orderBy(Query.Direction.DESCENDING)
                    recycler_view.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recycler_view.setHasFixedSize(true)
                    recycler_view.adapter = ProfileAdapter(ProfileListDTO)

                }


            }

            }

    override fun onStart() {
        super.onStart()

        var ProfileListDTO: ArrayList<Profiles> = ArrayList<Profiles>()
            var db = FirebaseFirestore.getInstance()


            db.collection("users").whereEqualTo("userId", "User")
                .get()
                .addOnSuccessListener { result ->

                    var profileDTO: Profiles

                    for (document in result) {
                        //Log.d(TAG,"$document.id}=>$document.data}")
                        profileDTO = document.toObject(Profiles::class.java)
                        ProfileListDTO.add(profileDTO)

                        recycler_view.layoutManager =
                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        recycler_view.setHasFixedSize(true)
                        recycler_view.adapter = ProfileAdapter(ProfileListDTO)
                    }
                }

    }}

//        }




