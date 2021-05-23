package com.example.boos.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.boos.R
import com.example.boos.start.Loginpage
import com.example.boos.utili.SessionMaintainence
import com.example.boos.utili.elements
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_deliverys_profile.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.logout
import kotlinx.android.synthetic.main.profile_fragment.nameuser
import kotlinx.android.synthetic.main.profile_fragment.phoneuser
import kotlinx.android.synthetic.main.profile_fragment.profileimage
import org.jetbrains.anko.startActivity

class DeliverysProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deliverys_profile)
        val upperString: String =
            SessionMaintainence.instance!!.fullname!!.substring(0, 1)
                .toUpperCase() + SessionMaintainence.instance!!.fullname!!.substring(1)
                .toLowerCase()
        nameuser.text = upperString
        phoneuser.text = "+91 " + SessionMaintainence.instance!!.phoneno
        Glide.with(this)
            .load(SessionMaintainence.instance!!.profilepic)
            .into(profileimage)
        edit2.setOnClickListener {
            startActivity<DelsessionActivity>()
        }
//        logout.setOnClickListener {
//            SessionMaintainence.instance!!.clearSession()
//            startActivity<Landingpage>()
//        }
        logout.setOnClickListener {
            val s = elements()
            if (s.isNetworkAvailable(this!!)) {
                try {
                    FirebaseMessaging.getInstance()
                        .unsubscribeFromTopic("delivery")
                        .addOnCompleteListener { task ->
                            var msg = "getString(R.string.msg_subscribed)"
                            if (!task.isSuccessful) {
                                msg = "getString(R.string.msg_subscribe_failed)"
                            }
                            SessionMaintainence.instance!!.clearSession()
                            startActivity<Loginpage>()
                            finish()
                            //                    Log.d(TAG, msg)
                        }
                } catch (e: Exception) {
                    SessionMaintainence.instance!!.clearSession()
                    startActivity<Loginpage>()
                    finish()
                }
            } else {
                SessionMaintainence.instance!!.clearSession()
                startActivity<Loginpage>()
                finish()
            }

        }
    }
}