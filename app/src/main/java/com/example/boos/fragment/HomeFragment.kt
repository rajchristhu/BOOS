package com.example.boos.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boos.R
import com.example.boos.adapter.cateadapter
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
        caterec!!.layoutManager = acceptHorizontalLayoutsss
        caterec!!.adapter = cateadapter( activity!!)
        // TODO: Use the ViewModel
    }

}