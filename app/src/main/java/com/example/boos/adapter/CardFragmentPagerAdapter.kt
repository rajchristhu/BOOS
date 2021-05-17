package com.example.boos.adapter

import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.boos.fragment.CardFragment
import com.example.boos.fragment.CardFragment.Companion.getInstance
import com.example.boos.model.dealModel
import java.util.*

class CardFragmentPagerAdapter(
    fm: FragmentManager?,
    baseElevation: Float,
    trendList: List<dealModel?>
) : FragmentStatePagerAdapter(
    fm!!
), CardAdapter {
    private val fragments: MutableList<CardFragment>
    private val baseElevation: Float
    override fun getBaseElevation(): Float {
        return baseElevation
    }

    override fun getCardViewAt(position: Int): CardView {
        return fragments[position].cardView!!
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return getInstance(position)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position)
        fragments[position] = fragment as CardFragment
        return fragment
    }

    fun addCardFragment(fragment: CardFragment) {
        fragments.add(fragment)
    }

    init {
        fragments = ArrayList()
        this.baseElevation = baseElevation
        for (i in trendList.indices) {
            addCardFragment(CardFragment(trendList as MutableList<dealModel>,i))
        }
    }
}