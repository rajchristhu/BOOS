package com.example.boos.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.airbnb.lottie.LottieAnimationView
import com.example.boos.R

/**
 * Created by SPECBEE on 8/4/2017.
 */

class SliderAdapter(
    private val context: Context,
    private val sliderImage: List<Int>,
    val slogan: ArrayList<String>
) : PagerAdapter() {
    override fun getCount(): Int {
        return sliderImage.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_slider, null)

        val image = view.findViewById(R.id.image) as LottieAnimationView
        val blogsl = view.findViewById(R.id.blogsl) as TextView
//        image.tag = position
//        image.setImageResource(sliderImage[position])
        when (position) {
            0 -> {
                image.setAnimation("one.json")

            }
            1 -> {
                image.setAnimation("two.json")

            }
            else -> {
                image.setAnimation("three.json")
            }
        }
        image.playAnimation()
        image.loop(true)

        blogsl.text = slogan[position]
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }
}
