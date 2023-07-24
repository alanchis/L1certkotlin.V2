package com.example.l1cert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoggedInActivity : AppCompatActivity() {

    lateinit var signOutBtn : Button
    private lateinit var  firebaseAuth: FirebaseAuth

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logged_in)

        signOutBtn = findViewById(R.id.signOutBtn)
        firebaseAuth= Firebase.auth

        //        GoogleSignin information
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this,gso)

        val account: GoogleSignInAccount?= GoogleSignIn
            .getLastSignedInAccount(this)

        if (account != null){

        // TODO


        } else{
//            goSignOut()
        }



        signOutBtn.setOnClickListener {
            signOut()
            goSignOut()
        }


    }

    private fun goSignOut() {
        gsc.signOut().addOnSuccessListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()

        }
    }

    override fun onBackPressed() {
        return
    }

    private fun signOut (){
        firebaseAuth.signOut()
        Toast.makeText(baseContext, "Signed out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent( this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}