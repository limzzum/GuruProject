package com.example.guruproject

//import android.support.v7.app.AppCompatActivity
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_loadview.*

//import kotlinx.android.synthetic.main.activity_upload_view.*

//import kotlinx.android.synthetic.main.activity_upload_view.*


class loadview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadview)


        finish_button.setOnClickListener {
val username= edit_name.getText().toString()
            val content=edit_content.getText().toString()
          //  val username=findViewById<EditText>(R.id.edit_name).toString()

           // val content=findViewById<EditText>(R.id.edit_content).toString()

            val intent=Intent(this,RecyclerView::class.java)
        intent.putExtra("key_username",username)
            intent.putExtra("key_content",content)
           setResult(Activity.RESULT_OK,intent)
finish()
            var user:Profiles = Profiles(
                //  R.drawable.ic_launcher_foreground ,
                //"Lim",
                userId="User",
                //R.drawable.ic_launcher_foreground,
                username = username,
                postContent = content
            )
            var db= FirebaseFirestore.getInstance()
            db.collection("users").add(user)
        }

    }
    private fun getPicture(){
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
    fun getImageFilePath(contentUri: Uri):String{
        var columnIndex=0
        val projection=arrayOf(MediaStore.Images.Media.DATA)
        val cursor=contentResolver.query(contentUri,projection,null,null,null)
        if(cursor!!.moveToFirst()){
            columnIndex=cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        }
        return cursor.getString(columnIndex)

    }
}
