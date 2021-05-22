package com.example.boos.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.example.boos.R
import com.example.boos.Room.*
import com.example.boos.fragment.*
import com.example.boos.locs.GpsTracker
import com.example.boos.map.MapActivity
import com.example.boos.util.GeocoderHandler
import com.example.boos.utili.SessionMaintainence
import com.google.android.gms.location.*
import com.google.firebase.firestore.FirebaseFirestore
import com.simform.custombottomnavigation.SSCustomBottomNavigation
import kotlinx.android.synthetic.main.activity_user.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.IOException
import java.util.*

class UserActivity : AppCompatActivity() {
    private var numVisibleChildren = 4
    lateinit var ViewModel: GroceryViewModel
    val list = mutableListOf<GroceryItems>()

    companion object {
        private const val ID_HOME = 1
        private const val ID_EXPLORE = 2
        private const val ID_MESSAGE = 3
        private const val ID_NOTIFICATION = 4
        private const val ID_ACCOUNT = 5
    }

    public var gpsTracker: GpsTracker? = null
    var lat: Double? = null
    var longs: Double? = null
    var instance = SessionMaintainence.instance!!

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var from = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        val groceryRepository = GroceryRepository(GroceryDatabase(this!!))

        val factory = GroceryViewModelFactory(groceryRepository)

        Glide.with(this)
            .load(SessionMaintainence.instance!!.profilepic)
            .placeholder(R.drawable.profilepng)
            .into(profileimage)
        ViewModel = ViewModelProviders.of(this, factory).get(GroceryViewModel::class.java)
        ViewModel!!.namess.observe(this, androidx.lifecycle.Observer {
            Glide.with(this)
                .load(SessionMaintainence.instance!!.profilepic)
                .placeholder(R.drawable.profilepng)
                .into(profileimage)
        })
        profileimage.setOnClickListener {
            startActivity<ProActivity>()
        }
        ViewModel.allGroceryItems().observe(this, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            ViewModel.addIssuePost(it)
            if (list.size != 0) {
                bottomNavigation.setCount(4, list.size.toString())
            } else {
                bottomNavigation.setCount(4, "0")
            }
        })

        ViewModel.mIssuePostLiveData.observe(this, androidx.lifecycle.Observer {


        })
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)
        when (from) {


            "" -> {
                start()
            }
            "map" -> {
                start()
            }
            "order" -> {
//                start()
//                changeFragment(TrackFragment(), "track")
            }
        }
        locationtext.setOnClickListener {
            if (checkPermissions()) {
//                sheet("1")
                startActivity<MapActivity>()
            } else {
                requestPermissions()
            }
        }


        from = try {
            intent.getStringExtra("from").toString()
        } catch (e: Exception) {
            ""
        }


        val bottomNavigation = bottomNavigation
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.of))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.track))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.home))
        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.cart))
        bottomNavigation.add(MeowBottomNavigation.Model(5, R.drawable.history))

        bottomNavigation.setOnShowListener {
        }



        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> changeFragment(OfferFrag(), "offer")
                2 -> changeFragment(TrackFrag(), "track")
                3 -> changeFragment(HomeFragment(), "home")
                4 -> changeFragment(CartFragment(), "cart")
                5 -> changeFragment(HistoryFragment(), "history")
                else -> changeFragment(HomeFragment(), "home")
            }
        }
        bottomNavigation.show(3)
//        changeFragment(HomeFragment(),"home")
    }


    fun dpToPx(dp: Float): Float = resources.displayMetrics.density * dp
    fun pxToDp(px: Float): Float = px / resources.displayMetrics.density
    private fun changeFragment(targetFragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment2, targetFragment, tag)
            .addToBackStack(null)
            //            .setTransitionStyle(FragmentTransaction.TRANSIT_NONE)
            .commit()
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ),
                        PERMISSION_ID
                    )
                }
            }
        }
    }


    fun start() {
        if (checkPermissions()) {
//            changeFragment(TrackFragment(), "track")

            //sheet("1")
            if (SessionMaintainence.instance!!.adds) {
                if (SessionMaintainence.instance!!.addressverify!!.count() >= 30) {
                    locationtext.text =
                        SessionMaintainence.instance!!.addressverify!!.take(30) + "..."
                } else {
                    locationtext.text = SessionMaintainence.instance!!.addressverify!!
                }
                changeFragment(HomeFragment(), "home")

//                changeFragment(GroceryFragment(), "grocery")
            } else {
                getLastLocation()
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                gpsTracker = GpsTracker(this)
                if (gpsTracker!!.canGetLocation()) {
                    val latitude: Double = gpsTracker!!.getLatitude()
                    val longitude: Double = gpsTracker!!.getLongitude()
                    lat = latitude
                    longs = longitude
                    instance.lat = latitude.toString()
                    instance.lang = longitude.toString()
                    val address = getAddressFromLocation(
                        lat!!,
                        longs!!,
                        applicationContext,
                        GeocoderHandler()
                    )
//                    tvLatitude!!.text = latitude.toString()
//                    tvLongitude!!.setText(longitude.toString())
                } else {
                    gpsTracker!!.showSettingsAlert()
                }


//                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
//                    var location: Location? = task.result
//                    if (location == null) {
//                        requestNewLocationData()
//                    } else {
//                        lat = location.latitude
//                        longs = location.longitude
//                        instance.lat = lat.toString()
//                        instance.lang = longs.toString()
//                        val address = getAddressFromLocation(
//                            lat!!,
//                            longs!!,
//                            applicationContext,
//                            GeocoderHandler()
//                        )
////                        locationtext.text = address
////
////                        changeFragment(MainFragment(), "mainfrag")
////                        findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
////                        findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()
//                    }
//                }
            } else {
                alert("Kindly enable your location ") {
                    title = "Alert"
                    isCancelable = false
                    okButton {
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivity(intent)
                    }
                }.show()
//                Toast.makeText(makeTextthis, "Turn on location", Toast.LENGTH_LONG).show()
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            lat = mLastLocation.latitude
            longs = mLastLocation.longitude
            instance.lat = lat.toString()
            instance.long = longs.toString()
            val address = getAddressFromLocation(
                lat!!,
                longs!!,
                applicationContext,
                GeocoderHandler()
            )

//            changeFragment(MainFragment(), "mainfrag")


//            findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
//            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }
    }

//    fun setData() {
//        val db = FirebaseFirestore.getInstance()
//
//        val document = db.collection("restaurants").document("tamils")
//        val s = Data("tamils")
//        document.set(s)
//            .addOnSuccessListener { _ ->
//                //Set Location After your document created
//                document.setLocation(10.5489, 79.6390, "geo")
//                toast("Added")
//            }
//            .addOnFailureListener { exception ->
//                //Document write failed
//            }
//    }


    private fun addressCheckss(toString: String): String {
        return if (!toString.isNullOrEmpty()) {
            " ,$toString"
        } else
            ""
    }

    //validation
    fun addressCheck(address: String): String {
        return if (!address.isNullOrEmpty()) {
            "$address,"
        } else
            ""
    }    //validation

    fun addressChecks(address: String): String {
        return if (!address.isNullOrEmpty()) {
            "$address"
        } else
            ""
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun getAddressFromLocation(
        latitude: Double,
        longitude: Double,
        context: Context?,
        handler: Handler?
    ): String? {
        var result: String? = null
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addressList =
                geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]
                val sb = java.lang.StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)) //.append("\n");
                }

                sb.append(address.getAddressLine(0))
                instance.lat = latitude.toString()
                instance.long = longitude.toString()
                result = sb.toString()
                instance.addressverify = result
                if (result!!.count() >= 30) {
                    locationtext.text = result.take(30) + "..."
                } else {
                    locationtext.text = result
                }
                changeFragment(HomeFragment(), "home")

//                changeFragment(MainFragment(), "mainfrag")

            }
        } catch (e: IOException) {
            Log.e("catch", e.toString())
            alert("Pick your location via map") {
                title = "Alert"
                isCancelable = false
                okButton { startActivity<MapActivity>() }
            }.show()
            Log.e("Location Address Loader", "Unable connect to Geocoder", e)
        } finally {
            val message = Message.obtain()
            message.target = handler
            if (result != null) {
                message.what = 1
                val bundle = Bundle()
//                alert("Pick your location via map") {
//                    title = "Alert"
//                    okButton {  startActivity<MapActivity>() }
//                }.show()
                bundle.putString("address", result)
                message.data = bundle
            } else {
                message.what = 1
                val bundle = Bundle()
//                alert("Pick your location via map") {
//                    title = "Alert"
//                    okButton {  startActivity<MapActivity>() }
//                }.show()
                result = " Unable to get address for this location."
                bundle.putString("address", result)
                message.data = bundle
            }
            message.sendToTarget()
        }



        return result
    }

    override fun onRestart() {
        super.onRestart()
        try {
            if (locationtext.text == "-----") {
                when (from) {
                    "" -> {
                        start()
                    }
                    "map" -> {
                        start()
                    }
                }
            }
        } catch (e: Exception) {
        }
//        when (from) {
//            "" -> {
//                start()
//            }
//            "map" -> {
//                start()
//            }
//        }
//        try {
//            val fragmentManager = supportFragmentManager
//            val currentFragment = fragmentManager.findFragmentById(R.id.fragment2)
//            if (currentFragment!!.tag == "mainfrag") {
//                if (from == "") {
//                    start()
//                }            }
//        } catch (e: Exception) {
//            if (from == "") {
//                start()
//            }
//        }
        Log.e("value", "restart")
    }

    fun rate() {
        val uri: Uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
//        try {
//            if (locationtext.text == "-----") {
//                when (from) {
//                    "" -> {
//                        start()
//                    }
//                    "map" -> {
//                        start()
//                    }
//                }
//            }
//        } catch (e: Exception) {
//        }
    }

    override fun onStart() {
        super.onStart()
        if (SessionMaintainence.instance!!.Uid!!.toString() == "") {
//            support.visibility = View.GONE
//            job.visibility = View.GONE
////            m_item_photos.visibility = View.GONE
//            post.visibility = View.GONE
        }
//        try {
//            val fragmentManager = supportFragmentManager
//            val currentFragment = fragmentManager.findFragmentById(R.id.fragment2)
//            if (currentFragment!!.tag == "mainfrag") {
//                if (from == "") {
//                    start()
//                }            }
//        } catch (e: Exception) {
//            if (from == "") {
//                start()
//            }
//        }
//        if (checkPermissions()) {
////                sheet("1")
//            if (SessionMaintainence.instance!!.adds) {
//                changeFragment(MainFragment(), "mainfrag")
//
//                if (SessionMaintainence.instance!!.addressverify!!.count() >= 30) {
//                    locationtext.text =
//                        SessionMaintainence.instance!!.addressverify!!.take(30) + "..."
//                } else {
//                    locationtext.text = SessionMaintainence.instance!!.addressverify!!
//                }
//
//            } else {
//                getLastLocation()
//            }
//        } else {
//            requestPermissions()
//        }

    }

}