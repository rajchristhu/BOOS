package com.example.boos.start

import android.R.attr.phoneNumber
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.boos.R
import com.example.boos.activity.UserActivity
import com.example.boos.utili.SessionMaintainence
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_otp_screen.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import java.util.concurrent.TimeUnit


class OtpScreen : AppCompatActivity() {
    private var mVerificationId: String? = null
    var check = false
    var firestoreDB: FirebaseFirestore? = null
    private var verificationId: String? = null
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    //firebase auth object
    private var mAuth: FirebaseAuth? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_screen)
        mAuth = FirebaseAuth.getInstance();
        firestoreDB = FirebaseFirestore.getInstance()

        count()
        val intent = intent
        val mobile = intent.getStringExtra("mobile")
        sendVerificationCode(mobile!!)
        otptext.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().count() == 6) {
                    val code: String = otptext.text.toString().trim()
                    verifyVerificationCode(code);
                } else {
                    errormsg.visibility = View.GONE
                }
            }
        })
        otptexts.text = "We have sent an OTP to +91-" + SessionMaintainence.instance!!.phoneno
        resend.setOnClickListener {
            if (check) {
                check = false
                try {
                    resendVerificationCode(SessionMaintainence.instance!!.phoneno!!, mResendToken!!)
                    count()
                } catch (e: Exception) {
                }
            }
        }
        back.setOnClickListener {
            finish()
        }
    }

    private fun sendVerificationCode(mobile: String) {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//            "+91$mobile",
//            20,
//            TimeUnit.SECONDS,
//            (Activity) TaskExecutors . MAIN_THREAD,
//            mCallbacks
//        )
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobile",  //phone number to be verified
            20,  // validity of the OTP
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD ,
            mCallbacks // onVerificationStateChangedCallback
        )
    }


    //the callback to detect the verification status
    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    otptext.setText(code)
                    //verifying the code
                    verifyVerificationCode(code)
                } else {
                    try {
                        alert("Your account has been auto verified by Google without OTP.") {
                            title = "Verification success"
                            isCancelable = false
                            okButton {
                                signInWithPhoneAuthCredential(phoneAuthCredential)
                            }
                        }.show()
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(this@OtpScreen, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                s: String,
                forceResendingToken: ForceResendingToken
            ) {
                super.onCodeSent(s, forceResendingToken)
                mResendToken = forceResendingToken

                //storing the verification id that is sent to the user
                mVerificationId = s
            }
        }


    private fun verifyVerificationCode(code: String) {
        //creating the credential
        try {
            val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)
            signInWithPhoneAuthCredential(credential)

        } catch (e: Exception) {
        }

        //signing the user
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this@OtpScreen,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!.uid
                        verifybuyer(currentFirebaseUser)
                        SessionMaintainence.instance!!.Uid = currentFirebaseUser
                        SessionMaintainence.instance!!.is_loggedin = true
                        //verification successful we will start the profile activity

                    } else {

                        //verification unsuccessful.. display an error message
                        var message = "Checking..."
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                            message = "Invalid code entered..."
                        }
                        errormsg.visibility = View.VISIBLE
                        errormsg.text = message

                    }
                })
    }

    private fun verifybuyer(currentFirebaseUser: String) {
        val docRef = firestoreDB!!.collection("buyer").document(currentFirebaseUser)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userType = document.data!!["user_type"].toString()
                    val name = document.data!!["name"].toString()
                    val profilepic = document.data!!["profileimage"].toString()
                    var shopId = ""
                    shopId = try {
                        document.data!!["shopId"].toString()
                    } catch (e: Exception) {
                        ""
                    }
                    SessionMaintainence.instance!!.userType = userType
                    SessionMaintainence.instance!!.fullname = name
                    SessionMaintainence.instance!!.profilepic = profilepic
                    SessionMaintainence.instance!!.dob = shopId
                    when (userType) {
                        "admin" -> {
                            val intent =
                                Intent(this@OtpScreen, AdminActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        "madmin" -> {
                            val intent =
                                Intent(this@OtpScreen, AdminActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        "owner" -> {
                            val intent =
                                Intent(this@OtpScreen, OwnerTabActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        "delivery" -> {
                            val intent =
                                Intent(this@OtpScreen, DeleveryBoy::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else -> {
                            val intent =
                                Intent(this@OtpScreen, UserActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                    }

                } else {
                    Log.d("TAG", "No such document")
                    profileCreate(SessionMaintainence.instance!!.phoneno!!, currentFirebaseUser)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    private fun profileCreate(phoneno: String, currentFirebaseUser: String) {
        val user = hashMapOf(
            "user_type" to "buyer",
            "uid" to currentFirebaseUser,
            "phoneno" to phoneno,
            "profileimage" to "",
            "orderstatus" to "0",
            "name" to ""
        )

// Add a new document with a generated ID
        firestoreDB!!.collection("buyer").document(currentFirebaseUser)
            .set(user)
            .addOnSuccessListener { documentReference ->
                SessionMaintainence.instance!!.userType = "buyer"
                SessionMaintainence.instance!!.fullname = ""
                val intent =
                    Intent(this@OtpScreen, UserActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
            }
    }

    //Resend OTP
    private fun resendVerificationCode(
        phoneNumber: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$phoneNumber",
            20, // Timeout duration
            TimeUnit.SECONDS,
            this,
            mCallbacks,
            token
        )
    }

    fun count() {
        val timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                resend.text =
                    Html.fromHtml("<font color='#424242'>Didn't receive the code? </font><font color='#9E9E9E'>resend now</font>")

                count.text = "0." + millisUntilFinished / 1000

            }

            override fun onFinish() {
                check = true
                resend.text =
                    Html.fromHtml("<font color='#424242'>Didn't receive the code? </font><font color='#ff0038'>resend now</font>")


            }
        }
        timer.start()

    }
}