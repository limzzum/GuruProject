package com.example.guruproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_upload_view.*
import java.io.File
private lateinit var database: DatabaseReference
database=Firebase.database.reference

class Uploadview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_view)
        uploadpicture.setOnClickListener {
            getPicture()
        }
    }

    fun getPicture(){
        val intent= Intent(Intent.ACTION_PICK)
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setType("image/*")
        startActivityForResult(intent, 1000)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==1000){
            val uri: Uri =data!!.data!!
        }
    }
    fun getImageFilePath(contentUri:Uri):String{
        var columnIndex=0
        val projection=arrayOf(MediaStore.Images.Media.DATA)
        val cursor=contentResolver.query(contentUri,projection,null,null,null)
        if(cursor!!.moveToFirst()){
            columnIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)

    }

}
}