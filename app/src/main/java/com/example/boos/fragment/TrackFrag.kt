package com.example.boos.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.boos.R
import kotlinx.android.synthetic.main.activity_user.*

class TrackFrag : Fragment() {

    companion object {
        fun newInstance() = TrackFrag()
    }

    private lateinit var viewModel: TrackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.track_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.bottomNavigation.show(2)
        activity!!.locationtext.visibility=View.GONE
        activity!!.imageView.visibility=View.GONE
        activity!!.textView13.text="Track Order"
    }

}