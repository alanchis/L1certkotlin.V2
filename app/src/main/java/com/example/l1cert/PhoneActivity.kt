package com.example.l1cert

import android.content.ContentValues.TAG
import android.content.EntityIterator
import android.content.Intent
//import android.os.Build.VERSION_CODES.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private lateinit var sendBtn : Button
    lateinit var verifyBtn : Button
    lateinit var inputPhone : EditText
    lateinit var  inputCode : EditText

    var verificationCode = ""





    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone)

        auth = Firebase.auth

        sendBtn = findViewById<Button>(R.id.sendOTPBtn)
        verifyBtn = findViewById(R.id.verifyBtn)
        inputPhone = findViewById(R.id.inputPhone)
        inputCode = findViewById(R.id.inputverifiedOTP)

        sendBtn.setOnClickListener {
            abc()
        }

        verifyBtn.setOnClickListener {
            signInWithSMSCode()
        }



        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                 storedVerificationId = verificationId
                 resendToken = token
            }
        }











    }

    fun abc(){

            val phoneString = inputPhone.text.toString()

            if (phoneString.length != 0){
                Toast.makeText(this,"${phoneString}", Toast.LENGTH_SHORT).show()

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneString) // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this) // Activity (for callback binding)
                    .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)

            } else {
                Toast.makeText(this,"type a phone number", Toast.LENGTH_SHORT).show()
            }






    }

    fun signInWithSMSCode(){

        val userEnterCode = inputCode.text.toString()
//        Toast.makeText(this,"${userEnterCode}", Toast.LENGTH_SHORT).show()
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!,userEnterCode)
        signInWithPhoneAuthCredential(credential)

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    Toast.makeText(this,"asd", Toast.LENGTH_SHORT).show()

                    val user = task.result?.user
                    val intent = Intent(this, LoggedInActivity::class.java )
                    startActivity(intent)
                    finish()


                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }


}