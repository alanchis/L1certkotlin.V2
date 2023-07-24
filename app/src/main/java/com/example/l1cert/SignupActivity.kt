package com.example.l1cert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    //declare late inits
    lateinit var emailText : EditText
    lateinit var  passwordText : EditText
    lateinit var  signUpBtn : Button
    private lateinit var  firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //declare variables text and buttons
        firebaseAuth= Firebase.auth
        emailText = findViewById(R.id.inputNewEmail)
        passwordText = findViewById(R.id.inputNewPassword)
        signUpBtn = findViewById(R.id.signUpBtn)

        signUpBtn.setOnClickListener {
            createAccount(emailText.text.toString(), passwordText.text.toString())

        }





    }

    private fun createAccount(email:String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener (this){ task ->
                if (task.isSuccessful){
                    sendEmailVerification()
                    Toast.makeText(baseContext, "Account created successfully, please verify your account", Toast.LENGTH_SHORT).show()
                    val intent = Intent( this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "Error when creating account ${task.exception}", Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun sendEmailVerification(){
        val user = firebaseAuth.currentUser!!
        user.sendEmailVerification().addOnCompleteListener(this){ task ->
        }
    }

}