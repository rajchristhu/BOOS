package com.example.boos.fragment

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.boos.R
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.offer_fragment.*

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
        try {
            activity!!.bottomNavigation.show(1)
            activity!!.locationtext.visibility = View.GONE
            activity!!.imageView.visibility = View.GONE
            activity!!.textView13.text = "Support"
            image.setAnimation("sis.json")
            image.playAnimation()
            image.loop(true)
        } catch (e: Exception) {
        }
        button.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://rajchristhu.wordpress.com"))
            startActivity(i)
        }
        floatingActionButton2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + "9597224715")
            startActivity(intent)
        }
    }

}