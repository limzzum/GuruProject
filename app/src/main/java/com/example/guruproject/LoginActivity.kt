package com.example.guruproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import com.google.firebase.ktx.Firebase
import com.google.rpc.context.AttributeContext
import kotlinx.android.synthetic.main.activity_login.*

var auth: FirebaseAuth=FirebaseAuth.getInstance()
class LoginActivity : AppCompatActivity() {

    val RC_SIGN_IN = 9001

    //private var firebaseAuth: FirebaseAuth? = null
    private val TAG : String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //firebaseAuth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.editTextEmail)
        val password = findViewById<EditText>(R.id.editTextPassword)


        buttonSignup.setOnClickListener {
            //loginEmail()

            if (email.text.toString().length == 0 || password.text.toString().length == 0){
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)

                            val intent = Intent(this@LoginActivity, TreeActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }

                        // ...
                    }
            }

        }

        //신규 회원가입
        textViewSignin.setOnClickListener {
            val intent1 = Intent(this@LoginActivity, CreateUserActivity::class.java)
            startActivity(intent1)
        }

    }

    fun updateUI(cUser : FirebaseUser? = null){
        if(cUser != null) {
            Toast.makeText(baseContext, "로그인이 되었습니다",
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(baseContext, "일치하는 사용자가 없습니다",
                Toast.LENGTH_SHORT).show()
        }
        editTextEmail.setText("")
        editTextPassword.setText("")
        hideKeyboard(editTextEmail)
        //Toast.makeText(this, "유저: "+cUser.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard(view: View) {
        view?.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



}