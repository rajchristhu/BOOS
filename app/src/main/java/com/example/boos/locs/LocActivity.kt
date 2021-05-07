package com.example.boos.locs

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.boos.R


class LocActivity : AppCompatActivity() {
    private var gpsTracker: GpsTracker? = null
    private var tvLatitude: TextView? = null
    private  var tvLongitude:TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loc)
        tvLatitude = findViewById(R.id.latitude) as TextView
        tvLongitude = findViewById(R.id.longitude) as TextView
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getLocation(view: View?) {
        gpsTracker = GpsTracker(this)
        if (gpsTracker!!.canGetLocation()) {
            val latitude: Double = gpsTracker!!.getLatitude()
            val longitude: Double = gpsTracker!!.getLongitude()
            tvLatitude!!.text = latitude.toString()
            tvLongitude!!.setText(longitude.toString())
        } else {
            gpsTracker!!.showSettingsAlert()
        }
    }
}