package com.example.boos.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.boos.R
import kotlinx.android.synthetic.main.activity_user.*

class OfferFrag : Fragment() {

    companion object {
        fun newInstance() = OfferFrag()
    }

    private lateinit var viewModel: OfferViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.offer_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.bottomNavigation.show(1)
        activity!!.locationtext.visibility=View.GONE
        activity!!.imageView.visibility=View.GONE
        activity!!.textView13.text="Offer"
    }

}