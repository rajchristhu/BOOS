package com.example.boos.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boos.R
import com.example.boos.Room.*
import com.example.boos.adapter.cartadapter
import kotlinx.android.synthetic.main.activity_item.*

class CartFragment : Fragment() {
    lateinit var ViewModel: GroceryViewModel
    val list = mutableListOf<GroceryItems>()

    companion object {
        fun newInstance() = CartFragment()
    }

    private lateinit var viewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val groceryRepository = GroceryRepository(GroceryDatabase(activity!!))

        val factory = GroceryViewModelFactory(groceryRepository)

        ViewModel = ViewModelProviders.of(this, factory).get(GroceryViewModel::class.java)
        ViewModel.allGroceryItems().observe(this, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            ViewModel.addIssuePost(it)

            val acceptHorizontalLayoutsss11 =
                LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
            recyclerViews!!.layoutManager = acceptHorizontalLayoutsss11
            recyclerViews!!.adapter = cartadapter(activity!!, list)
        })
    }

}