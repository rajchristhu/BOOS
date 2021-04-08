package com.example.boos.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.example.boos.R
import com.simform.custombottomnavigation.SSCustomBottomNavigation
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.toast

class UserActivity : AppCompatActivity() {
    private var numVisibleChildren = 4
    companion object {
        private const val ID_HOME = 1
        private const val ID_EXPLORE = 2
        private const val ID_MESSAGE = 3
        private const val ID_NOTIFICATION = 4
        private const val ID_ACCOUNT = 5
    }
  

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

    bottomNavigation.apply {

            add(SSCustomBottomNavigation.Model(ID_HOME, R.drawable.menud, "Home"))
            add(SSCustomBottomNavigation.Model(ID_EXPLORE, R.drawable.menud, "Favorite"))
            add(SSCustomBottomNavigation.Model(ID_MESSAGE, R.drawable.menud, "Chat"))
            add(
                SSCustomBottomNavigation.Model(
                    ID_NOTIFICATION,
                    R.drawable.menud,
                    "Notification"
                )
            )
            add(SSCustomBottomNavigation.Model(ID_ACCOUNT, R.drawable.menud, "Profile"))


            setOnShowListener {
                val name = when (it.id) {
                    ID_HOME -> "Home"
                    ID_EXPLORE -> "Explore"
                    ID_MESSAGE -> "Message"
                    ID_NOTIFICATION -> "Notification"
                    ID_ACCOUNT -> "Account"
                    else -> ""
                }

                val bgColor = when (it.id) {
                    ID_HOME -> ContextCompat.getColor(this@UserActivity, R.color.color_home_bg)
                    ID_EXPLORE -> ContextCompat.getColor(this@UserActivity, R.color.color_favorite_bg)
                    ID_MESSAGE -> ContextCompat.getColor(this@UserActivity, R.color.color_chat_bg)
                    ID_NOTIFICATION -> ContextCompat.getColor(this@UserActivity, R.color.color_notification_bg)
                    ID_ACCOUNT -> ContextCompat.getColor(this@UserActivity, R.color.color_profile_bg)
                    else -> ContextCompat.getColor(this@UserActivity, R.color.colorPrimary)
                }

//                tvSelected.text = getString(R.string.main_page_selected, name)
//                binding.lnrLayout.setBackgroundColor(bgColor)
            }

            setOnClickMenuListener {
                val name = when (it.id) {
                    ID_HOME -> "HOME"
                    ID_EXPLORE -> "EXPLORE"
                    ID_MESSAGE -> "MESSAGE"
                    ID_NOTIFICATION -> "NOTIFICATION"
                    ID_ACCOUNT -> "ACCOUNT"
                    else -> ""
                }
                toast(name)
            }

            setOnReselectListener {
                Toast.makeText(context, "item ${it.id} is reselected.", Toast.LENGTH_LONG).show()
            }


        }

    }




    fun dpToPx(dp: Float): Float = resources.displayMetrics.density * dp
    fun pxToDp(px: Float): Float = px / resources.displayMetrics.density
}