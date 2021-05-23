package com.example.boos.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boos.R
import com.example.boos.adapter.orderAdapter
import com.example.boos.model.orederModel
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.history_fragment.*
import kotlinx.android.synthetic.main.track_fragment.*
import kotlinx.android.synthetic.main.track_fragment.imageView14
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }
    var sessionMaintainence = SessionMaintainence.instance
    var firestoreDB: FirebaseFirestore? = null
    var orderList: MutableList<orederModel> = mutableListOf<orederModel>()
    var count = ""
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.bottomNavigation.show(5)
        activity!!.locationtext.visibility=View.GONE
        activity!!.imageView.visibility=View.GONE
        activity!!.textView13.text="History Page"
        orderList.clear()
        firestoreDB = FirebaseFirestore.getInstance()
//        try {
//            activity!!.itemcard.visibility = View.GONE
//
//            activity!!.main.visibility = View.GONE
//            activity!!.sub.visibility = View.VISIBLE
//            activity!!.equal_navigation_bars.visibility = View.GONE
//            activity!!.head.text = "Order History"
//        } catch (e: Exception) {
//        }

        val sdf = SimpleDateFormat("MMM dd,yyyy")
        val currentDateandTime: String = sdf.format(Date())

        val sdfs = SimpleDateFormat("EEEE")
        val currentDateandTimes: String = sdfs.format(Date())
        day.text = currentDateandTimes
        date.text = currentDateandTime
        firestoreDB!!.collection("order")
            .whereEqualTo("userId", SessionMaintainence!!.instance!!.Uid)
            .get()
            .addOnSuccessListener {
                for (i in it) {
                    val s = i.toObject(orederModel::class.java)
                    orderList.add(s)
                }
                var monthList: List<orederModel> = orderList.filter { s -> s.status == "close" }
                if (monthList.isEmpty()) {
                    try {
                        imageView14.visibility = View.VISIBLE
                        orderrec.visibility = View.GONE
                        textView58.visibility = View.GONE
                        imageView14.setAnimation("empty.json")
                        imageView14.playAnimation()
                        imageView14.loop(true)
                    } catch (e: Exception) {
                    }
                } else {
                    imageView14.visibility = View.GONE
                    orderrec.visibility = View.VISIBLE
                    textView58.visibility = View.VISIBLE
                }
                count = monthList.size.toString()
                orderco.text = count
                orderrec.layoutManager = LinearLayoutManager(context)
                orderrec.adapter = orderAdapter(monthList as MutableList<orederModel>, context!!)
            }
            .addOnFailureListener {

            }
    }

    override fun onStart() {
        super.onStart()
        orderList.clear()
    }

}