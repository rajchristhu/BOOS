package com.example.boos.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.boos.R
import com.github.kwasow.bottomnavigationcircles.BottomNavigationCircles
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
        val bottomNavigation = bottomNavigation
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.of))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.track))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.home))
        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.cart))
        bottomNavigation.add(MeowBottomNavigation.Model(5, R.drawable.history))
        bottomNavigation.show(3)

        bottomNavigation.setOnShowListener {
        }

        bottomNavigation.setOnClickMenuListener {
        }
    }




    fun dpToPx(dp: Float): Float = resources.displayMetrics.density * dp
    fun pxToDp(px: Float): Float = px / resources.displayMetrics.density
}