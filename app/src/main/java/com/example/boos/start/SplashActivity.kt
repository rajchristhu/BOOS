
package com.example.boos.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.boos.MainActivity
import com.example.boos.R
import com.example.boos.activity.UserActivity
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.jetbrains.anko.startActivity
import kotlin.coroutines.CoroutineContext

class SplashActivity() : AppCompatActivity(){

    var firestoreDB: FirebaseFirestore? = null
    var bg = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Glide.with(this).load("file:///android_asset/uiyt.gif").into(imageView);
        val instance = SessionMaintainence.instance!!

        Handler().postDelayed({
            try {
                if (instance.Uid == "") {
                    if (instance.dans) {
                        startActivity<Loginpage>()
                        finish()

                    } else {
                        startActivity<GetStart>()
                        finish()
                    }

                } else {
                    try {
                        when (SessionMaintainence.instance!!.userType) {
                            "admin" -> {
                                SessionMaintainence.instance!!.adds = false

                                startActivity<AdminActivity>()
                                finish()
                            }
//                            "madmin" -> {
//                                SessionMaintainence.instance!!.adds = false
//
//                                startActivity<AdminTabActivity>()
//                              finish()
//                            }

                            "owner" -> {
                                SessionMaintainence.instance!!.adds = false

                                startActivity<OwnerTabActivity>()
                                finish()
                            }
                            "delivery" -> {
                                SessionMaintainence.instance!!.adds = false

                                startActivity<DeleveryBoy>()
                                finish()
                            }
                            else -> {
                                SessionMaintainence.instance!!.adds = false

                                startActivity<UserActivity>()
                                finish()
                            }
                        }

                    } catch (e: Exception) {
                    }

                }

            } catch (e: Exception) {
                bg = true
            }
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
        }, 2000) // 3000 is the delayed time in milliseconds.
    }
}