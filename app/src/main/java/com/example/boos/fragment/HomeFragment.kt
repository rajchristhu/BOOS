package com.example.boos.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.boos.R
import com.example.boos.adapter.*
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val acceptHorizontalLayoutsss =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val acceptHorizontalLayoutsss1 =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        caterec!!.layoutManager = acceptHorizontalLayoutsss
        caterec!!.adapter = cateadapter(activity!!)

        offer!!.layoutManager = acceptHorizontalLayoutsss1
        offer!!.adapter = offercardadapter(activity!!)

        val pagerAdapter =
            CardFragmentPagerAdapter(activity!!.supportFragmentManager, dpToPixels(2, activity!!))
        val fragmentCardShadowTransformer =
            ShadowTransformer(viewPager, pagerAdapter)
        fragmentCardShadowTransformer.enableScaling(true)
        adddeal.setOnClickListener {

        }
        viewPager.setAdapter(pagerAdapter)
        viewPager.setPageTransformer(false, fragmentCardShadowTransformer)
        viewPager.offscreenPageLimit = 3


        val imageSlider: ImageSlider = slider

        val slideModels: MutableList<SlideModel> = ArrayList()
        slideModels.add(
            SlideModel(
                "https://p.ecopetit.cat/wpic/lpic/26-263518_tumblr-photography-wallpaper-rocks-on-earth-background.jpg",
                "1 Image"
            )
        )
        slideModels.add(
            SlideModel(
                "https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg",
                "2 Image"
            )
        )
        slideModels.add(
            SlideModel(
                "https://live.staticflickr.com/7006/6621416427_8504865e6a_z.jpg",
                "3 Image"
            )
        )
        slideModels.add(
            SlideModel(
                "https://c4.wallpaperflare.com/wallpaper/662/618/496/natur-2560x1600-sceneries-wallpaper-preview.jpg",
                "4 Image"
            )
        )
        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP)
        val acceptHorizontalLayoutsss11 =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        caterec1!!.layoutManager = acceptHorizontalLayoutsss11
        caterec1!!.adapter = papularadapter(activity!!,slideModels)


    }
    fun dpToPixels(dp: Int, context: Context): Float {
        return dp * context.getResources().getDisplayMetrics().density
    }

}