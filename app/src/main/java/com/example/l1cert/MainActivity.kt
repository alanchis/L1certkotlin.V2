package com.example.l1cert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    //declare floating action buttons late init
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc : GoogleSignInClient
//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest

    lateinit var btnGoogle: FloatingActionButton
    lateinit var btnFacebook: FloatingActionButton
    lateinit var btnPhone: FloatingActionButton
    lateinit var btnSignIn : Button
    lateinit var btnSignUp : TextView
    lateinit var btnForgotPassword : TextView
    lateinit var emailText : EditText
    lateinit var passwordText : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // declare button and text variables
        btnGoogle = findViewById(R.id.googleBtn)
        btnFacebook = findViewById(R.id.facebookBtn)
        btnPhone = findViewById(R.id.floatingActionButton5)
        btnSignIn = findViewById(R.id.signInBtn)
        btnSignUp = findViewById(R.id.signUpText)
        btnForgotPassword = findViewById(R.id.textView3)
        emailText = findViewById(R.id.inputEmail)
        passwordText = findViewById(R.id.inputPassword)

        firebaseAuth = Firebase.auth

//        GoogleSignin information
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this,gso)

        val account:GoogleSignInAccount?= GoogleSignIn
            .getLastSignedInAccount(this)

        if(account != null){
            goToHome()
        }






        //Click listeners
        btnGoogle.setOnClickListener {
//            Toast.makeText(this, "Google button clicked", Toast.LENGTH_SHORT).show()
            goToSignIn()


        }
        btnFacebook.setOnClickListener {
            Toast.makeText(this, "Facebook button clicked", Toast.LENGTH_SHORT).show()
        }
        btnPhone.setOnClickListener {
//            Toast.makeText(this, "Phone  button clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent( this, PhoneActivity::class.java)
            startActivity(intent)
        }
        btnSignIn.setOnClickListener {
//            Toast.makeText(this, "SignIn  button clicked", Toast.LENGTH_SHORT).show()
            if (emailText.text.toString().length != 0 && passwordText.text.toString().length != 0){
                signIn(emailText.text.toString(), passwordText.text.toString())
            } else{
                Toast.makeText(this, "Fill the required fields", Toast.LENGTH_SHORT).show()

            }


        }
        btnSignUp.setOnClickListener {
            Toast.makeText(this, "Signup  text clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent( this, SignupActivity::class.java)
            startActivity(intent)
        }
        btnForgotPassword.setOnClickListener {
//            Toast.makeText(this, "Forgot password text clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent( this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }


    }

    private fun goToSignIn() {

        val signInIntent = gsc.signInIntent

        startActivityForResult(signInIntent, 1000)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000){
//            val task: Task<GoogleSignInAccount>  = GoogleSignIn
//                .getSignedInAccountFromIntent(data)
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)



                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{

                        if(it.isSuccessful){

                            goToHome()

                        }else{

                        }
                    }}










            } catch (e:java.lang.Exception){
                Toast.makeText(this, "${e.message} Error logging in", Toast.LENGTH_SHORT).show()
            }


        }





    }

    private fun goToHome() {

        val intent = Intent(this, LoggedInActivity::class.java)
        startActivity(intent)
        finish()


    }

    private fun signIn (email:String, password: String){
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){ task ->
            if (task.isSuccessful){
                val user = firebaseAuth.currentUser
                Toast.makeText(baseContext, "Succesful login", Toast.LENGTH_SHORT).show()
                // intent a la activity login
                val intent = Intent (this, LoggedInActivity::class.java)
                    startActivity(intent)
            } else {
                Toast.makeText(baseContext, "Error, check credentials", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser != null){
            startActivity(Intent(this , LoggedInActivity::class.java))

        }
    }




}
