package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class uploadactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        upload_button.setOnClickListener {
            val ToUploadView= Intent(this@UploadActivity,Uploadview::class.java)
            startActivity(ToUploadView)
        }
    }
}
