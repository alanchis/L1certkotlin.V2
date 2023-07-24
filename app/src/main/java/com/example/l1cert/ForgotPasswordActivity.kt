package com.example.l1cert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var emailText: EditText
    lateinit var resetBtn : Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var  authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        firebaseAuth = Firebase.auth
        emailText  = findViewById(R.id.inputEmailRecovery)
        resetBtn = findViewById(R.id.resetPasswordBtn)

        resetBtn.setOnClickListener {
//            Toast.makeText(this, "${emailText.text.toString()}", Toast.LENGTH_SHORT).show()
            if (emailText.text.toString().length != 0){

                sendPasswordReset(emailText.text.toString())

        } else {
            Toast.makeText(this, "Fill the required field", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun sendPasswordReset(email: String){
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(){ task ->
                if (task.isSuccessful){

                    Toast.makeText(this, "Check you email for resetting your password", Toast.LENGTH_SHORT).show()
                    val intent = Intent( this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()

                }
            }
    }
}