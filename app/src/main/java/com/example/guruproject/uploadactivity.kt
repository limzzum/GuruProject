package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_uploadactivity.*

class uploadactivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uploadactivity)
        upload_button.setOnClickListener {
            val ToUploadView= Intent(this@uploadactivity,uploadview::class.java)
            startActivity(ToUploadView)
        }
    }
}
