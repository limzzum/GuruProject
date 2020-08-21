package com.example.guruproject


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_loadview.*



class loadview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadview)

        cancel_button.setOnClickListener {
            val intent1=Intent(this,RecyclerView::class.java)
            startActivity(intent1)
        }
        finish_button.setOnClickListener {
val username= edit_name.getText().toString()
            val content=edit_content.getText().toString()

            var user:Profiles = Profiles(
                userId="User",
                username = username,
                postContent = content
            )
            var db= FirebaseFirestore.getInstance()
            db.collection("users").add(user)
            val intent=Intent(this,RecyclerView::class.java)

            startActivity(intent)
        }

    }

}
