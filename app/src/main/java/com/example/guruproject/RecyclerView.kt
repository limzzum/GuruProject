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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.activity_loadview.*
import kotlinx.android.synthetic.main.activity_recycler_view.*


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

//        var profiles = arrayListOf(
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루"),
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루") ,
//            Profiles(R.drawable.ic_launcher_foreground,"zzum"," 정현",R.drawable.ic_launcher_foreground,"좋은하루")
//        )
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

                //profiles.add(Profiles(UserId,Username,PostContent))

                //   profiles=document.toObject(Profiles::class.java)
                //profileList.add(profiles)

            }
    }
//        }

    override fun onStart() {
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
                    super.onStart()

                }

            }
    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if(requestCode==200){
//      val username =data?.getStringExtra("key_name")
//            var content =data?.getStringExtra("key_content")
//            var user:Profiles = Profiles(
//                //  R.drawable.ic_launcher_foreground ,
//                //"Lim",
//                userId="User",
//                //R.drawable.ic_launcher_foreground,
//                username = username,
//                postContent = content
//            )
//            var db=FirebaseFirestore.getInstance()
//            db.collection("users").add(user)
//    }
//        super.onActivityResult(requestCode, resultCode, data)
//    }}