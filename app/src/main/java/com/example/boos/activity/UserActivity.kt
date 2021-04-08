package com.example.boos.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.boos.R
import com.simform.custombottomnavigation.SSCustomBottomNavigation
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    private var numVisibleChildren = 4

    companion object {
        private const val MAX_MENU_ITEMS = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        initMenuItemsVisibility()

    }

    private fun initMenuItemsVisibility() {
        for (i in 0 until bottomNavigationView.menu.size()) {
            bottomNavigationView.menu.getItem(i).isVisible = i < numVisibleChildren
        }
    }


    fun dpToPx(dp: Float): Float = resources.displayMetrics.density * dp
    fun pxToDp(px: Float): Float = px / resources.displayMetrics.density
}