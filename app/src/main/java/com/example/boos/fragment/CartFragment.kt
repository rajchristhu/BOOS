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
import kotlinx.android.synthetic.main.activity_item.constraintLayout3
import kotlinx.android.synthetic.main.activity_item.recyclerViews
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.cart_fragment.*

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
        activity!!.bottomNavigation.show(4)
        activity!!.locationtext.visibility = View.GONE
        activity!!.imageView.visibility = View.GONE
        activity!!.textView13.text = "My Cart"
        val factory = GroceryViewModelFactory(groceryRepository)
        if (list.size != 0) {
            constraintLayout3.visibility = View.VISIBLE
            image.visibility = View.GONE
            textView16.visibility = View.GONE
        } else {
            constraintLayout3.visibility = View.GONE
            image.visibility = View.VISIBLE
            textView16.visibility = View.VISIBLE
            image.setAnimation("emca.json")
            image.playAnimation()
            image.loop(true)
        }
        ViewModel = ViewModelProviders.of(this, factory).get(GroceryViewModel::class.java)
        ViewModel.allGroceryItems().observe(this, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            ViewModel.addIssuePost(it)
            if (list.size != 0) {
                constraintLayout3.visibility = View.VISIBLE
                image.visibility = View.GONE
                textView16.visibility = View.GONE
                val acceptHorizontalLayoutsss11 =
                    LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
                recyclerViews!!.layoutManager = acceptHorizontalLayoutsss11
                recyclerViews!!.adapter = cartadapter(activity!!, list)
                recyclerViews!!.adapter!!.notifyDataSetChanged()
            }
            else
            {
                constraintLayout3.visibility = View.GONE
                image.visibility = View.VISIBLE
                textView16.visibility = View.VISIBLE
                image.setAnimation("emca.json")
                image.playAnimation()
                image.loop(true)
            }
        })
    }

}