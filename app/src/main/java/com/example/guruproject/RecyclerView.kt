//package com.example.guruproject
//
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//
//class RecyclerView : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_recycler_view)
//    }
//}
package com.example.guruproject

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.primitives.UnsignedLongs.sort
import com.google.firebase.firestore.FirebaseFirestore
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


        all_list.setOnClickListener {
            val intent = Intent(this, TreeActivity::class.java)
            startActivity(intent)
        }
        my_list.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }
        user_info.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent, 200)
        }
        upload_button.setOnClickListener {
            val intent = Intent(this, loadview::class.java)
            startActivity(intent)
        }
        upload.setOnClickListener{
            val intent = Intent(this, RecyclerView::class.java)
            startActivity(intent)
            finish()
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

                    recycler_view.layoutManager =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    recycler_view.setHasFixedSize(true)
                    recycler_view.adapter = ProfileAdapter(ProfileListDTO)
                    //  var profilelist=Profiles(userId=UserId, username = Username,postContent =  PostContent)
                    //profiles.add(Profiles(userId=UserId, username = Username,postContent =  PostContent))
                    // profiles.add(profilelist)
                }


            }
                //profiles.add(Profiles(UserId,Username,PostContent))

                //   profiles=document.toObject(Profiles::class.java)
                //profileList.add(profiles)

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


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 200) {
//            var ProfileListDTO: ArrayList<Profiles> = ArrayList<Profiles>()
//            var db = FirebaseFirestore.getInstance()
//
//
//            db.collection("users").whereEqualTo("userId", "User")
//                .get()
//                .addOnSuccessListener { result ->
//
//                    var profileDTO: Profiles
//
//                    for (document in result) {
//                        //Log.d(TAG,"$document.id}=>$document.data}")
//                        profileDTO = document.toObject(Profiles::class.java)
//                        ProfileListDTO.add(profileDTO)
//
//                        recycler_view.layoutManager =
//                            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//                        recycler_view.setHasFixedSize(true)
//                        recycler_view.adapter = ProfileAdapter(ProfileListDTO)
//
//
//                    }
//
//                }
//            super.onActivityResult(requestCode, resultCode, data)
//        }

