package com.example.boos.activity

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ckdroid.geofirequery.GeoQuery
import com.ckdroid.geofirequery.model.Distance
import com.ckdroid.geofirequery.utils.BoundingBoxUtils
import com.example.boos.R
import com.example.boos.adapter.cateadapter
import com.example.boos.adapter.papularadapter
import com.example.boos.model.cateModel
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_show.*
import kotlinx.android.synthetic.main.home_fragment.*

class ShowActivity : AppCompatActivity() {
    var cateList: MutableList<cateModel> = mutableListOf<cateModel>()
    var type = ""
    var popList: MutableList<cateModel> = mutableListOf<cateModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)
        type = try {
            intent.getStringExtra("type")!!
        } catch (e: Exception) {
            ""
        }
        imageView.setOnClickListener {
            finish()
        }
        if (type == "1") {
            textView13.text = "Categories"
            caterecs()
        } else {
            textView13.text = "Popular"
            setpop()
        }
    }

    private fun caterecs() {
        cateList.clear()
        val distanceForRadius = Distance(12.0, BoundingBoxUtils.DistanceUnit.KILOMETERS)
        val db = FirebaseFirestore.getInstance()
        val targetLocation =
            Location("") //provider name is unnecessary

        targetLocation.latitude =
            SessionMaintainence.instance!!.lat!!.toDouble()//your coords of course

        targetLocation.longitude = SessionMaintainence.instance!!.long!!.toDouble()
        val geoQuery = GeoQuery()
            .collection("catekadai")
//        .whereEqualTo("status","approved")
//        .whereEqualTo("country","IN")
            .whereNearToLocation(targetLocation, distanceForRadius, "geo")
//        .startAfter(lastDocument) //optinal (for pagination)
        geoQuery.get()
            .addOnSuccessListener { addedOrModifiedDataList, removedList ->
                for (i in addedOrModifiedDataList) {
                    val s = i.toObject(cateModel::class.java)
                    cateList.add(s!!)
                }
                try {
                    val layoutManager = AutoFitGridLayoutManager(this!!, 500)

                    val acceptHorizontalLayoutsss =
                        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    vdkj!!.layoutManager = layoutManager
                    vdkj!!.adapter = cateadapter(this!!, cateList,"2")
                } catch (e: Exception) {
                }
            }
    }

    private fun setpop() {

        popList.clear()
        val distanceForRadius = Distance(12.0, BoundingBoxUtils.DistanceUnit.KILOMETERS)
        val db = FirebaseFirestore.getInstance()
        val targetLocation =
            Location("") //provider name is unnecessary

        targetLocation.latitude =
            SessionMaintainence.instance!!.lat!!.toDouble()//your coords of course

        targetLocation.longitude = SessionMaintainence.instance!!.long!!.toDouble()
        val geoQuery = GeoQuery()
            .collection("pop")
//        .whereEqualTo("status","approved")
//        .whereEqualTo("country","IN")
            .whereNearToLocation(targetLocation, distanceForRadius, "geo")
//        .startAfter(lastDocument) //optinal (for pagination)
        geoQuery.get()
            .addOnSuccessListener { addedOrModifiedDataList, removedList ->
                for (i in addedOrModifiedDataList) {
                    val s = i.toObject(cateModel::class.java)
                    popList.add(s!!)
                }
//                val acceptHorizontalLayoutsss =
//                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//                caterec!!.layoutManager = acceptHorizontalLayoutsss
//                caterec!!.adapter = cateadapter(activity!!, cateList)
                try {
                    val layoutManager = AutoFitGridLayoutManager(this!!, 500)

                    val acceptHorizontalLayoutsss11 =
                        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    caterec1!!.layoutManager = layoutManager
                    caterec1!!.adapter = papularadapter(this!!, popList,"2")
                } catch (e: Exception) {
                }
//                setOffer()
            }
    }

}