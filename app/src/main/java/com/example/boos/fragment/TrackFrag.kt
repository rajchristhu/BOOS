package com.example.boos.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boos.R
import com.example.boos.adapter.trackAdapter
import com.example.boos.model.orederModel
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.track_fragment.*
import org.jetbrains.anko.startActivity

class TrackFrag : Fragment() {

    companion object {
        fun newInstance() = TrackFrag()
    }
    var firestoreDB: FirebaseFirestore? = null
    var orderList: MutableList<orederModel> = mutableListOf<orederModel>()
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
        firestoreDB = FirebaseFirestore.getInstance()

        imageView14.setOnClickListener {
            changeFragment(HomeFragment(), "home")
        }
        firestoreDB!!.collection("order")
            .whereEqualTo("userId", SessionMaintainence!!.instance!!.Uid)
            .get()
            .addOnSuccessListener {
                orderList.clear()
                for (i in it) {
                    val s = i.toObject(orederModel::class.java)
                    orderList.add(s)
                }
                try {
                    var monthList: List<orederModel> = orderList.filter { s -> s.status != "close" }
                    if (monthList.isEmpty()) {
                        try {
                            imageView14.visibility = View.VISIBLE
                            trackor.visibility = View.GONE
                            imageView14.setAnimation("orderbtn.json")
                            imageView14.playAnimation()
                            imageView14.loop(true)
                        } catch (e: Exception) {
                        }
                    } else {
                        imageView14.visibility = View.GONE
                        trackor.visibility = View.VISIBLE
                    }

                    trackor.layoutManager = LinearLayoutManager(context)
                    trackor.adapter = trackAdapter(monthList as MutableList<orederModel>, context!!)
                } catch (e: Exception) {
                }
            }
            .addOnFailureListener {

            }
    }

    override fun onStart() {
        super.onStart()
        orderList.clear()
    }

    private fun changeFragment(targetFragment: Fragment, tag: String) {
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment2, targetFragment, tag)
            .addToBackStack(null)
            //            .setTransitionStyle(FragmentTransaction.TRANSIT_NONE)
            .commit()
    }

}