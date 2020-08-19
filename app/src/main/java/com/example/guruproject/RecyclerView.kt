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
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_recycler_view.*


class RecyclerView : AppCompatActivity() {
//var TAG="데이터"
//    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        var profiles = arrayListOf(
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루") ,
            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루")
        )

//        var db = FirebaseFirestore.getInstance()
//
//        var user = Profiles(
//               R.drawable.ic_launcher_foreground ,
//            "Lim",
//            "정현",
//             R.drawable.ic_launcher_foreground,
//            "제발제발"
//        )
//        db.collection("users").add(user)
//        db.collection("users")
//            .get()
//            .addOnSuccessListener { result ->
//
//                var UserId = ""
//                var Username = ""
//                var PostContent = ""
//
//                for (document in result) {
//                    //Log.d(TAG,"$document.id}=>$document.data}")
//
//                    UserId = document.data["userId"].toString()
//                    Username = document.data["username"].toString()
//                    PostContent = document.data["postContent"].toString()
//                  //  var profilelist=Profiles(userId=UserId, username = Username,postContent =  PostContent)
//                   profiles.add(Profiles(userId=UserId, username = Username,postContent =  PostContent))
//                   // profiles.add(profilelist)
//                }
//
//                //profiles.add(Profiles(UserId,Username,PostContent))
//
//                //   profiles=document.toObject(Profiles::class.java)
//                //profileList.add(profiles)
//
//            }

        recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = ProfileAdapter(profiles)
        all_list.setOnClickListener{
            val intent=Intent(this,TreeActivity::class.java)
            startActivity(intent)
        }
        my_list.setOnClickListener{
            val intent=Intent(this,CalendarActivity::class.java)
            startActivity(intent)
        }
        my_info.setOnClickListener{
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
//        upload_button.setOnClickListener {
//            val intent = Intent(this, loadview::class.java)
//            startActivity(intent)
//        }
    }
}