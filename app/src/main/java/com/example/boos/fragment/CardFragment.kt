package com.example.boos.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.boos.R
import com.example.boos.model.dealModel

class CardFragment(val trendList: MutableList<dealModel>, val i: Int) : Fragment() {
    val cardView: CardView? = null
    private var img: ImageView? = null

   

    @SuppressLint("DefaultLocale")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_viewpager, container, false)
        img = view.findViewById<View>(R.id.payimages) as ImageView
        Glide.with(this)
            .load(trendList[i].image)
            .placeholder(R.drawable.index)
            .into(img!!)
        //        cardView.setMaxCardElevation(cardView.getCardElevation() * CardAdapter.MAX_ELEVATION_FACTOR);
        return view
    }

    companion object {
        @JvmStatic
        fun getInstance(position: Int): Fragment {
            val f = CardFragment()
            val args = Bundle()
            args.putInt("position", position)
            f.arguments = args
            return f
        }

        private fun CardFragment(): CardFragment {
            TODO("Not yet implemented")
        }
    }
}