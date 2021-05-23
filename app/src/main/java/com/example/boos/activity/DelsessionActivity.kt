package com.example.boos.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boos.R
import com.example.boos.adapter.deliveryadapters
import com.example.boos.model.orederModel
import com.example.boos.start.DeleveryBoy
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_delsession.*
import org.jetbrains.anko.startActivity
import java.io.File
import java.io.FileWriter
import java.util.stream.Collectors

class DelsessionActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    private lateinit var acceptHorizontalLayout: LinearLayoutManager
    var orderList: MutableList<orederModel> = mutableListOf<orederModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delsession)
        firestoreDB = FirebaseFirestore.getInstance()
        acceptHorizontalLayout =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        firestoreDB!!.collection("order")
//            .whereEqualTo("deliId", SessionMaintainence!!.instance!!.Uid)
//            .get()
//            .addOnSuccessListener {
//
//            }
//            .addOnFailureListener {
//            }
        segmented {

            // set initial checked segment (null by default)
            initialCheckedIndex = 0

            // init with segments programmatically without RadioButton as a child in xml
            initWithItems {

                listOf("Take", "Delivery", "Complete")

//                listOf("Breakfast", "Lunch", "Dinner")
            }

            // notifies when segment was checked
            onSegmentChecked { segment ->
                when (segment.text) {
                    "Take" -> {

//                        if (sd.isNotEmpty()) {
                        try {
                            when (SessionMaintainence!!.instance!!.userType) {
                                "ndelivery" -> {
                                    val sdg = orderList.filter { it -> it.status == "open" }
                                    val sd = sdg.filter { it -> it.oru == "na" }
                                    delrey!!.layoutManager = acceptHorizontalLayout
                                    delrey!!.adapter = deliveryadapters(
                                        this@DelsessionActivity,
                                        sd as MutableList<orederModel>
                                    )
                                }
                                "mdelivery" -> {
                                    val sdg = orderList.filter { it -> it.status == "open" }
                                    val sd = sdg.filter { it -> it.oru == "ma" }
                                    delrey!!.layoutManager = acceptHorizontalLayout
                                    delrey!!.adapter = deliveryadapters(
                                        this@DelsessionActivity,
                                        sd as MutableList<orederModel>
                                    )
                                }
                                "sdelivery" -> {
                                    val sdg = orderList.filter { it -> it.status == "open" }
                                    val sd = sdg.filter { it -> it.oru == "sa" }
                                    delrey!!.layoutManager = acceptHorizontalLayout
                                    delrey!!.adapter = deliveryadapters(
                                        this@DelsessionActivity,
                                        sd as MutableList<orederModel>
                                    )
                                }
                                else -> {
                                    val sdg = orderList.filter { it -> it.status == "open" }
                                    val sd = sdg.filter { it -> it.oru == "" }
                                    delrey!!.layoutManager = acceptHorizontalLayout
                                    delrey!!.adapter = deliveryadapters(
                                        this@DelsessionActivity,
                                        sd as MutableList<orederModel>
                                    )
                                }
                            }
//                                rect.layoutManager = LinearLayoutManager(context)
//                                rect.adapter = rectadapt(
//                                    sd as MutableList<KadaiFoodModel>,
//                                    this@PkgActivity,
//                                    ids!!
//                                )
                        } catch (e: Exception) {
                        }
//                        } else {
//                            try {
//                                imageds.visibility = View.VISIBLE
//                                try {
//                                    imageds.visibility = View.VISIBLE
//                                    imageds.setAnimation("no.json")
//
//                                    imageds.playAnimation()
//                                    imageds.loop(true)
//                                } catch (e: Exception) {
//                                }
////                                imageds.visibility = View.GONE
//                                acceptjobpostHorizontalAdapter =
//                                    resitadapt(
//                                        sd as MutableList<KadaiFoodModel>,
//                                        this@ItemActivity,
//                                        cartList,
//                                        ids!!,
//                                        status,
//                                        name!!,
//                                        this@ItemActivity
//                                    )
//                                val acceptHorizontalLayoutsss =
//                                    LinearLayoutManager(
//                                        this@ItemActivity,
//                                        LinearLayoutManager.VERTICAL,
//                                        false
//                                    )
//                                recyitem.layoutManager = acceptHorizontalLayoutsss
//                                recyitem.adapter = acceptjobpostHorizontalAdapter
////                                rect.layoutManager = LinearLayoutManager(context)
////                                rect.adapter = rectadapt(
////                                    sd as MutableList<KadaiFoodModel>,
////                                    this@PkgActivity,
////                                    ids!!
////                                )
//                            } catch (e: Exception) {
//                            }
//                        }
                    }
                    "Delivery" -> {

//                        if (sd.isNotEmpty()) {
                        try {
                            if (SessionMaintainence!!.instance!!.userType == "sdelivery") {
                                val sdg = orderList.filter { it -> it.status == "onway" }
                                val sd = sdg.filter { it -> it.oru == "sa" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            } else if (SessionMaintainence!!.instance!!.userType == "ndelivery") {
                                val sdg = orderList.filter { it -> it.status == "onway" }
                                val sd = sdg.filter { it -> it.oru == "na" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            } else if (SessionMaintainence!!.instance!!.userType == "mdelivery") {
                                val sdg = orderList.filter { it -> it.status == "onway" }
                                val sd = sdg.filter { it -> it.oru == "ma" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            } else {
                                val sdg = orderList.filter { it -> it.status == "onway" }
                                val sd = sdg.filter { it -> it.oru == "" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            }

//                            val sdg = orderList.filter { it -> it.status == "onway" }
//                            delrey!!.layoutManager = acceptHorizontalLayout
//                            delrey!!.adapter = deliveryadapters(
//                                this@DelsessionActivity,
//                                sdg as MutableList<orederModel>
//                            )

//                                rect.layoutManager = LinearLayoutManager(context)
//                                rect.adapter = rectadapt(
//                                    sd as MutableList<KadaiFoodModel>,
//                                    this@PkgActivity,
//                                    ids!!
//                                )
                        } catch (e: Exception) {
                        }
//                        } else {
//                            try {
//                                imageds.visibility = View.VISIBLE
//                                try {
//                                    imageds.visibility = View.VISIBLE
//                                    imageds.setAnimation("no.json")
//
//                                    imageds.playAnimation()
//                                    imageds.loop(true)
//                                } catch (e: Exception) {
//                                }
////                                imageds.visibility = View.GONE
//                                acceptjobpostHorizontalAdapter =
//                                    resitadapt(
//                                        sd as MutableList<KadaiFoodModel>,
//                                        this@ItemActivity,
//                                        cartList,
//                                        ids!!,
//                                        status,
//                                        name!!,
//                                        this@ItemActivity
//                                    )
//                                val acceptHorizontalLayoutsss =
//                                    LinearLayoutManager(
//                                        this@ItemActivity,
//                                        LinearLayoutManager.VERTICAL,
//                                        false
//                                    )
//                                recyitem.layoutManager = acceptHorizontalLayoutsss
//                                recyitem.adapter = acceptjobpostHorizontalAdapter
////                                rect.layoutManager = LinearLayoutManager(context)
////                                rect.adapter = rectadapt(
////                                    sd as MutableList<KadaiFoodModel>,
////                                    this@PkgActivity,
////                                    ids!!
////                                )
//                            } catch (e: Exception) {
//                            }
//                        }
                    }
                    "Complete" -> {

                        try {
                            if (SessionMaintainence!!.instance!!.userType == "mdelivery") {
                                val sdg = orderList.filter { it -> it.status == "close" }
                                val sd = sdg.filter { it -> it.oru == "ma" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            } else if (SessionMaintainence!!.instance!!.userType == "ndelivery") {
                                val sdg = orderList.filter { it -> it.status == "close" }
                                val sd = sdg.filter { it -> it.oru == "na" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            } else if (SessionMaintainence!!.instance!!.userType == "sdelivery") {
                                val sdg = orderList.filter { it -> it.status == "close" }
                                val sd = sdg.filter { it -> it.oru == "sa" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            } else {
                                val sdg = orderList.filter { it -> it.status == "close" }
                                val sd = sdg.filter { it -> it.oru == "" }
                                delrey!!.layoutManager = acceptHorizontalLayout
                                delrey!!.adapter = deliveryadapters(
                                    this@DelsessionActivity,
                                    sd as MutableList<orederModel>
                                )
                            }
//                            val sdg = orderList.filter { it -> it.status == "close" }
//                            val so = sdg.sortedByDescending { it.postedTime }
//                            delrey!!.layoutManager = acceptHorizontalLayout
//                            delrey!!.adapter = deliveryadapters(
//                                this@DelsessionActivity,
//                                so as MutableList<orederModel>
//                            )

                        } catch (e: Exception) {
                        }


                    }

                }
                Log.d("creageek:segmented", "Segment ${segment.text} checked")
            }
            // notifies when segment was unchecked
            onSegmentUnchecked { segment ->
                Log.d("creageek:segmented", "Segment ${segment.text} unchecked")
            }
            // notifies when segment was rechecked
            onSegmentRechecked { segment ->
                Log.d("creageek:segmented", "Segment ${segment.text} rechecked")
            }
        }



        firestoreDB!!.collection("order")

//            .whereEqualTo("deliId", SessionMaintainence!!.instance!!.Uid)
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
                            if (s.deliId == SessionMaintainence.instance!!.Uid) {
                                orderList.add(s)
                            }

                        }
                        DocumentChange.Type.MODIFIED -> {
                            val s = dc.document.toObject(orederModel::class.java)
                            if (s.deliId == SessionMaintainence.instance!!.Uid) {
                                orderList.add(s)
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            val s = dc.document.toObject(orederModel::class.java)
                            orderList.remove(s)
                        }
                    }
                }


//                delrey!!.layoutManager = acceptHorizontalLayout
//                delrey!!.adapter = deliveryadapters(this, orderList as MutableList<orederModel>)
//                val sdg = orderList.filter { it -> it.status == "open" }
//                delrey!!.layoutManager = acceptHorizontalLayout
//                delrey!!.adapter = deliveryadapters(this, sdg as MutableList<orederModel>)
                if (SessionMaintainence!!.instance!!.userType == "ndelivery") {
                    val sdg = orderList.filter { it -> it.status == "open" }
                    val sd = sdg.filter { it -> it.oru == "na" }
                    delrey!!.layoutManager = acceptHorizontalLayout
                    delrey!!.adapter = deliveryadapters(
                        this@DelsessionActivity,
                        sd as MutableList<orederModel>
                    )
                } else if (SessionMaintainence!!.instance!!.userType == "mdelivery") {
                    val sdg = orderList.filter { it -> it.status == "open" }
                    val sd = sdg.filter { it -> it.oru == "ma" }
                    delrey!!.layoutManager = acceptHorizontalLayout
                    delrey!!.adapter = deliveryadapters(
                        this@DelsessionActivity,
                        sd as MutableList<orederModel>
                    )
                } else if (SessionMaintainence!!.instance!!.userType == "sdelivery") {
                    val sdg = orderList.filter { it -> it.status == "open" }
                    val sd = sdg.filter { it -> it.oru == "sa" }
                    delrey!!.layoutManager = acceptHorizontalLayout
                    delrey!!.adapter = deliveryadapters(
                        this@DelsessionActivity,
                        sd as MutableList<orederModel>
                    )
                } else {
                    val sdg = orderList.filter { it -> it.status == "open" }
                    val sd = sdg.filter { it -> it.oru == "" }
                    delrey!!.layoutManager = acceptHorizontalLayout
                    delrey!!.adapter = deliveryadapters(
                        this@DelsessionActivity,
                        sd as MutableList<orederModel>
                    )
                }
            }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<DeleveryBoy>()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun mains() {
        var path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_MOVIES
        )
        val file = File(path, "sto1.csv")
        val writer = FileWriter(file)
        val test: MutableList<String> = ArrayList()
        test.add("Word1")
        test.add("Word2")
        test.add("Word3")
        test.add("Word4")
        val collect: String = test.stream().collect(Collectors.joining(","))
        Log.e("jsc", collect)
        writer.write(collect)
        writer.close()
    }

}