package com.example.boos.start

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boos.R
import com.example.boos.activity.DeliverysProfile
import com.example.boos.adapter.deliveryadapter
import com.example.boos.model.orederModel
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_delevery_boy.*
import org.jetbrains.anko.startActivity

class DeleveryBoy : AppCompatActivity() {
    private lateinit var acceptHorizontalLayout: LinearLayoutManager
    var firestoreDB: FirebaseFirestore? = null
    var orderList: MutableList<orederModel> = mutableListOf<orederModel>()
    var orderListn: MutableList<orederModel> = mutableListOf<orederModel>()
    var orderListm: MutableList<orederModel> = mutableListOf<orederModel>()
    var orderLists: MutableList<orederModel> = mutableListOf<orederModel>()
    var orderListt: MutableList<orederModel> = mutableListOf<orederModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delevery_boy)
        acceptHorizontalLayout =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        firestoreDB = FirebaseFirestore.getInstance()
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("delivery")
                .addOnCompleteListener { task ->
                    var msg = "Success"
                    if (!task.isSuccessful) {
                        msg = "failed"
                    }
                    Log.d("TAG", msg)
                }
        } catch (e: Exception) {
        }

        firestoreDB!!.collection("order")
            .whereEqualTo("deliId", "")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w("TAG", "listen:error", e)
                    return@addSnapshotListener
                }
//                kadaiList.clear()
                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {

                        DocumentChange.Type.ADDED -> {
                            val s = dc.document.toObject(orederModel::class.java)
                            orderList.add(s)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            val s = dc.document.toObject(orederModel::class.java)
                            orderList.add(s)
                        }
                        DocumentChange.Type.REMOVED -> {
                            val s = dc.document.toObject(orederModel::class.java)
                            orderList.remove(s)
                        }
                    }
                }

                val sdg = orderList.filter { it -> it.status == "open" }
                val sdgs = orderList.filter { it -> it.status == "open" }
                delre!!.layoutManager = acceptHorizontalLayout
                when (SessionMaintainence.instance!!.userType) {
                    "ndelivery" -> {
                        orderLists.clear()
                        for (i in sdg) {
                            when (i.oru) {


                                "na" -> {

                                    orderLists.add(i)

                                }
                            }

                        }
                        delre!!.adapter =
                            deliveryadapter(this, orderLists as MutableList<orederModel>)

                    }
                    "mdelivery" -> {
                        orderListn.clear()
                        for (i in sdg) {
                            when (i.oru) {


                                "ma" -> {

                                    orderListn.add(i)

                                }
                            }

                        }
                        delre!!.adapter =
                            deliveryadapter(this, orderListn as MutableList<orederModel>)
                    }
                    "sdelivery" -> {
                        orderListm.clear()
                        for (i in sdg) {
                            when (i.oru) {


                                "sa" -> {

                                    orderListm.add(i)

                                }
                            }

                        }
                        delre!!.adapter =
                            deliveryadapter(this, orderListm as MutableList<orederModel>)
                    }
                    else -> {
                        orderListt.clear()
                        for (i in sdg) {

                            when (i.oru) {
                                "" -> {

                                    orderListt.add(i)

                                }
                            }

                        }
                        delre!!.adapter =
                            deliveryadapter(this, orderListt as MutableList<orederModel>)
                    }
                }
            }

//        delre!!.layoutManager = acceptHorizontalLayout
//        delre!!.adapter = deleveryadapter(this)
        profileimage.setOnClickListener {
            startActivity<DeliverysProfile>()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}