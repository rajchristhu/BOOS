package com.example.boos.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.addCallback
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.boos.R
import com.example.boos.activity.UserActivity
import com.example.boos.adapter.SliderAdapter
import com.example.boos.utili.SessionMaintainence
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_get_start.*
import org.jetbrains.anko.startActivity

class GetStart : AppCompatActivity() {
//    val navOptions = NavOptions.Builder().setPopUpTo(R.id.login, true).build()
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    var slogan: ArrayList<String>? = null

    lateinit var mGoogleSignInOptions: GoogleSignInOptions




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_start)
        val sliderImage = ArrayList<Int>()
        sliderImage.add(R.drawable.newo)
        sliderImage.add(R.drawable.newo)
        sliderImage.add(R.drawable.newo)
        google_button.setOnClickListener {
            if (google_button.text == "Start") {
                startActivity<Loginpage>()
                finish()
            } else {
                viewPagers.setCurrentItem(getItem(+1), true); //getItem(-1) for previous
            }
        }


        slogan = arrayListOf<String>("Shop Now!!", "Festival Offers!!", "New Gift!!")
        viewPagers.adapter = SliderAdapter(this!!, sliderImage, slogan!!)
        indicator.setupWithViewPager(viewPagers, true)
        // our extra tab
        // our extra tab

        // remove the `OnTabSelectedListener` created by `setupWithViewPager()`

        // remove the `OnTabSelectedListener` created by `setupWithViewPager()`
        indicator.clearOnTabSelectedListeners()

        // add our own `OnTabSelectedListener`

        // add our own `OnTabSelectedListener`
        indicator.addOnTabSelectedListener(object : TabLayout.ViewPagerOnTabSelectedListener(viewPagers) {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == viewPagers.adapter!!.count - 1) {
//                    toast("hi")
                    google_button.text = "Start"
                    // special case for the last tab in the list
//                    val intent = Intent(this, Land::class.java)
//                    startActivity(intent)
                } else {
                    // otherwise, handle as normal
                    google_button.text = "Next"

                    super.onTabSelected(tab)
                }
            }
        })
        button2.setOnClickListener {
//            Navigation.findNavController(requireActivity(), R.id.fragment)
//                .navigate(
//                    R.id.action_login_to_land2, null,
//                    navOptions
//                )
        }

//        google_button.setOnClickListener {
////            signIn()
//        }
        val callback = this.onBackPressedDispatcher.addCallback(this) {
            finishAffinity()
        }
        callback.isEnabled = true
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this!!, mGoogleSignInOptions)
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
            }
        }
    }

    private fun getItem(i: Int): Int {
        return viewPagers.currentItem + i
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser
                val instance = SessionMaintainence.instance!!
                instance.Uid = user!!.uid
                instance.profilepic = user.photoUrl.toString()
                instance.fullname = user.displayName
//                Navigation.findNavController(this, R.id.fragment)
//                    .navigate(
//                        R.id.action_login_to_land2, null,
//                        navOptions
//                    )
            } else {
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            startActivity<UserActivity>()
//            finish()
//
//        }
    }
}