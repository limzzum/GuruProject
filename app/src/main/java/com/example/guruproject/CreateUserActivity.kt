package com.example.guruproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private val TAG : String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)

        //회원이시면 여기서 로그인해주세요 intent->loginactivity
        textViewSignin.setOnClickListener {
            val intent = Intent(this@CreateUserActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        //회원가입 버튼을 누르면
        buttonSignup.setOnClickListener {

            if (email.text.toString().length == 0 || password.text.toString().length == 0) {
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser

                            Toast.makeText(this, "완료되었습니다. 로그인 화면으로 돌아갑니다.", Toast.LENGTH_SHORT).show()

                            //회원가입후 로그인 화면으로 돌아감
                            val intent1 = Intent(this@CreateUserActivity, LoginActivity::class.java)
                            startActivity(intent1)
                            //updateUI(user)
                            // 아니면 액티비티를 닫아 버린다.
                            //finish()
                            //overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)

                            // db구축
                            val db = Firebase.firestore
                            var m = hashMapOf(
                                "air" to 0,
                                "soil" to 0,
                                "water" to 0
                            )
                            auth.currentUser?.let  { user -> //currentUser가 null이 아닐 때 실행
                                db.collection(user.uid).document("treesave").set(m)
                            }
                            var m1 = hashMapOf(
                                "air" to 4,
                                "soil" to 4,
                                "water" to 4,
                                "tree" to 0
                            )
                            auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
                                db.collection(user.uid).document("treeicon").set(m1)
                            }
                            var a = hashMapOf(
                                "category" to "air",
                                "date" to Calendar.getInstance().get(Calendar.DATE).toInt()-1,
                                "isDone" to false,
                                "text" to ""
                            )
                            var w = hashMapOf(
                                "category" to "water",
                                "date" to Calendar.getInstance().get(Calendar.DATE).toInt()-1,
                                "isDone" to false,
                                "text" to ""
                            )
                            var s = hashMapOf(
                                "category" to "soil",
                                "date" to Calendar.getInstance().get(Calendar.DATE).toInt()-1,
                                "isDone" to false,
                                "text" to ""
                            )//com.example.guruproject.
                            auth.currentUser?.let { user -> //currentUser가 null이 아닐 때 실행
                                db.collection(user.uid).document(user.uid).collection("treemission")
                                    .document("air").set(a)
                                db.collection(user.uid).document(user.uid).collection("treemission")
                                    .document("water").set(w)
                                db.collection(user.uid).document(user.uid).collection("treemission")
                                    .document("soil").set(s)
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            //updateUI(null)
                            //입력필드 초기화
                            email?.setText("")
                            password?.setText("")
                            email.requestFocus()
                        }
                    }
            }
        }
    }
}