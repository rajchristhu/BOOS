package com.example.boos.map


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.ResultReceiver
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.example.boos.R

import com.ceseagod.showcase.map.AppUtils
import com.example.boos.activity.UserActivity
import com.example.boos.util.GeocoderHandler
import com.example.boos.utili.SessionMaintainence

import com.google.android.gms.common.*
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import kotlinx.android.synthetic.main.activity_map.*
import org.jetbrains.anko.startActivity
import java.io.IOException
import java.util.*

open class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    internal lateinit var mContext: Context
    internal lateinit var mLocationMarkerText: TextView
    private var mCenterLatLong: LatLng? = null

    var oiu = ""
    var dty = ""

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private var mResultReceiver: AddressResultReceiver? = null

    /**
     * The formatted location address.
     */
    protected var mAddressOutput: String? = null
    protected var mAreaOutput: String? = null
    protected var mCityOutput: String? = null
    protected var mStateOutput: String? = null
    internal lateinit var mLocationAddress: EditText
    internal lateinit var etPlace: EditText
    internal lateinit var mLocationText: TextView
    internal lateinit var mToolbar: Toolbar
    var autocompleteFragment: AutocompleteSupportFragment? = null
    var opt22 = ""
    var opt11 = ""
    var lats = ""
    var longs = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        imageView42.setOnClickListener {
            finish()
        }
        autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment
        Places.initialize(applicationContext, "AIzaSyCCETWtYGEMee8I2YXl5pGSvTROY3ZIKMw")
        etPlace =
            autocompleteFragment!!.view?.findViewById(R.id.places_autocomplete_search_input) as EditText
        etPlace.textSize = 16F
        // Create a new Places client instance.
        val placesClient = Places.createClient(this)
        autocompleteFragment!!.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
            )
        )

        autocompleteFragment!!.setOnPlaceSelectedListener(object : PlaceSelectionListener,
            com.google.android.libraries.places.widget.listener.PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                val sss = p0.addressComponents
                centerMapOnMyLocation(p0.latLng)
                dones.visibility = View.INVISIBLE
//                val s=p0.address
//                autocompleteFragment!!.setText(p0.address)
            }

            override fun onPlaceSelected(p0: com.google.android.gms.location.places.Place?) {
                val ss = p0!!.address
                try {
                    Log.e("sssdddwd", ss.toString())
                } catch (e: Exception) {
                    Log.e("sssdddwd", "err")
                }
            }

            override fun onError(p0: Status) {
                val ss = p0
            }

        })

        mContext = this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        mLocationMarkerText = findViewById<TextView>(R.id.locationMarkertext)
        mLocationAddress = findViewById<EditText>(R.id.Address)
        mLocationText = findViewById<TextView>(R.id.Locality)
        mLocationText.setOnClickListener { openAutocompleteActivity() }
        mapFragment!!.getMapAsync(this)
        mResultReceiver = AddressResultReceiver(Handler())
        done.setOnClickListener {
//            startActivity<MarketActivity>("from" to "map")
            finish()
        }
        dones.setOnClickListener {
//            mWordViewModel!!.deleteall()
            startActivity<UserActivity>("from" to "map")
//            finish()

        }
        if (checkPlayServices()) {
            // If this check succeeds, proceed with normal processing.
            // Otherwise, prompt user to get valid Play Services APK.
            if (!AppUtils.isLocationEnabled(mContext)) {
                // notify user
                val dialog = AlertDialog.Builder(mContext)
                dialog.setMessage("Location not enabled!")
                dialog.setPositiveButton("Open location settings") { paramDialogInterface, paramInt ->
                    val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(myIntent)
                }
                dialog.setNegativeButton("Cancel") { paramDialogInterface, paramInt ->
                    // TODO Auto-generated method stub
                }
                dialog.show()
            }
            buildGoogleApiClient()
        } else {
            Toast.makeText(mContext, "Location not supported in this device", Toast.LENGTH_SHORT)
                .show()
        }

    }


    @SuppressLint("SetTextI18n")
    override fun onMapReady(googleMap: GoogleMap) {
        Log.d(TAG, "OnMapReady")
        mMap = googleMap

        mMap!!.setOnCameraChangeListener { cameraPosition ->
            Log.d("Camera postion change" + "", cameraPosition.toString() + "")
            mCenterLatLong = cameraPosition.target


            mMap!!.clear()

            try {
                val mLocation = Location("")
                mLocation.latitude = mCenterLatLong!!.latitude
                mLocation.longitude = mCenterLatLong!!.longitude
                try {
                    lats = intent.getStringExtra("lat").toString()
                    longs = intent.getStringExtra("long").toString()
                } catch (e: Exception) {
                }
                if (lats.isNotEmpty() && lats.isNullOrBlank() && longs.isNotEmpty() && longs.isNullOrBlank()) {
                    etPlace.isEnabled = false
                    centerMapOnMyLocations(lats, longs)
                } else {
                    startIntentService(mLocation)
                }
//                mLocationMarkerText.text =
//                    "Lat : " + mCenterLatLong!!.latitude + "," + "Long : " + mCenterLatLong!!.longitude
                val address = getAddressFromLocation(
                    mCenterLatLong!!.latitude,
                    mCenterLatLong!!.longitude,
                    applicationContext,
                    GeocoderHandler()
                )
                SessionMaintainence.instance!!.addressverify = address
                SessionMaintainence.instance!!.adds = true
                if (oiu != "1") {
//                    if (opt11 == "") {
                        opt1!!.text = ""
                        etPlace.setText("")
                        opt1!!.text = address
                        SessionMaintainence.instance!!.addressverify = address
                        if (SessionMaintainence.instance!!.addressverify != "") {
                            dones.visibility = View.VISIBLE

                        }
//                    }
                }
                else
                {
                    opt1!!.text = ""
                    etPlace.setText("")
                    opt1!!.text = address
                    SessionMaintainence.instance!!.addressverify = address
                    if (SessionMaintainence.instance!!.addressverify != "") {
                        dones.visibility = View.VISIBLE

                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

    }

    override fun onConnected(bundle: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
            mGoogleApiClient
        )
        if (mLastLocation != null) {
            changeMap(mLastLocation)
            Log.d(TAG, "ON connected")

        } else
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this
                )

            } catch (e: Exception) {
                e.printStackTrace()
            }

        try {
            val mLocationRequest = LocationRequest()
            mLocationRequest.interval = 10000
            mLocationRequest.fastestInterval = 5000
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onConnectionSuspended(i: Int) {
        Log.i(TAG, "Connection suspended")
        mGoogleApiClient!!.connect()
    }

    override fun onLocationChanged(location: Location?) {
        try {
            if (location != null)
                changeMap(location)
            LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }


    @Synchronized
    protected fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }

    override fun onStart() {
        super.onStart()
        try {
            mGoogleApiClient!!.connect()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            opt11 = intent.getStringExtra("val1").toString()
            opt22 = intent.getStringExtra("val2").toString()

        } catch (e: Exception) {
            opt11 = ""
            opt22 = ""
        }
        if (opt11.isNotEmpty() && opt11.isNullOrBlank()) {
            dssd.visibility = View.GONE
            textView73.visibility = View.GONE
            opt1.text = opt11
        }
        if (opt22.isNotEmpty()&& opt22.isNullOrBlank()) {
            opt2.setText(opt22)
        }

    }

    override fun onStop() {
        super.onStop()
        try {

        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    private fun checkPlayServices(): Boolean {
        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(
                    resultCode, this,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                ).show()
            } else {
                //finish();
            }
            return false
        }
        return true
    }

    private fun centerMapOnMyLocation(latLng: LatLng?) {
        if (mMap != null) {
            oiu = "1"
            mMap!!.uiSettings.isZoomControlsEnabled = false
            val latLong = LatLng(latLng!!.latitude, latLng.longitude)
            val cameraPosition = CameraPosition.Builder()
                .target(latLong).zoom(19f).tilt(70f).build()
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
//                requestPermission("android.permission.ACCESS_FINE_LOCATION", "initLocation");

                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
            mMap!!.uiSettings.isCompassEnabled = false
            mMap!!.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(cameraPosition)
            )
        }
    }

    private fun centerMapOnMyLocations(lat: String, longss: String) {
        if (mMap != null) {
            oiu = "1"
            dty = "1"
            mMap!!.uiSettings.isZoomControlsEnabled = false
            val latLong = LatLng(lat.toDouble(), longss.toDouble())
            val cameraPosition = CameraPosition.Builder()
                .target(latLong).zoom(19f).tilt(70f).build()
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
            mMap!!.uiSettings.isCompassEnabled = false
            mMap!!.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(cameraPosition)
            )
            lats = ""
            longs = ""
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changeMap(location: Location) {
        oiu = "0"
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        // check if map is created successfully or not
        if (mMap != null) {
            mMap!!.uiSettings.isZoomControlsEnabled = false
            val latLong: LatLng = LatLng(location.latitude, location.longitude)


            val cameraPosition = CameraPosition.Builder()
                .target(latLong).zoom(19f).tilt(70f).build()

            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
            mMap!!.uiSettings.isCompassEnabled = false
            mMap!!.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(cameraPosition)
            )

//            mLocationMarkerText.text = "Lat : " + location.latitude + "," + "Long : " + location.longitude
//            val s = getAddressFromLocation(
//                mCenterLatLong!!.latitude,
//                mCenterLatLong!!.longitude,
//                applicationContext,
//                GeocoderHandler()
//            )
//            mLocationText.text = s
//            startIntentService(location)


        } else {
            Toast.makeText(
                applicationContext,
                "Sorry! unable to create maps", Toast.LENGTH_SHORT
            )
                .show()
        }

    }


    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    internal inner class AddressResultReceiver(handler: Handler) : ResultReceiver(handler) {

        /**
         * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY)

            mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA)

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY)
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STREET)

            displayAddressOutput()

            // Show a toast message if an address was found.
            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
                //  showToast(getString(R.string.address_found));


            }


        }

    }

    /**
     * Updates the address in the UI.
     */
    private fun displayAddressOutput() {
        //  mLocationAddressTextView.setText(mAddressOutput);
        try {
            if (mAreaOutput != null)
            // mLocationText.setText(mAreaOutput+ "");

                mLocationAddress.setText(mAddressOutput)
            //mLocationText.setText(mAreaOutput);
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    private fun startIntentService(mLocation: Location) {
        // Create an intent for passing to the intent service responsible for fetching the address.
        val intent = Intent(this, FetchAddressIntentService::class.java)

        // Pass the result receiver as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver)

        // Pass the location data as an extra to the service.
        intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation)

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent)
    }


    private fun openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                .build(this)
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE)
        } catch (e: GooglePlayServicesRepairableException) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(
                this, e.connectionStatusCode,
                0 /* requestCode */
            ).show()
        } catch (e: GooglePlayServicesNotAvailableException) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            val message =
                "Google Play Services is not available: " + GoogleApiAvailability.getInstance()
                    .getErrorString(
                        e.errorCode
                    )

            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }

    }

    /**
     * Called after the autocomplete activity has finished to return its result.
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == Activity.RESULT_OK) {
                // Get the user's selected place from the Intent.
                val place = PlaceAutocomplete.getPlace(mContext, data!!)

                // TODO call location based filter


                val latLong: LatLng


                latLong = place.latLng

                //mLocationText.setText(place.getName() + "");

                val cameraPosition = CameraPosition.Builder()
                    .target(latLong).zoom(19f).tilt(70f).build()

                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                mMap!!.isMyLocationEnabled = true
                mMap!!.animateCamera(
                    CameraUpdateFactory
                        .newCameraPosition(cameraPosition)
                )


            }


        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            val status = PlaceAutocomplete.getStatus(mContext, data!!)
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // Indicates that the activity closed before a selection was made. For example if
            // the user pressed the back button.
        }
    }

    fun getAddressFromLocation(
        latitude: Double, longitude: Double,
        context: Context, handler: Handler
    ): String? {
        oiu = "0"
        val geocoder = Geocoder(context, Locale.getDefault())
        var resultss: String? = null
        var address: Address? = null
        try {
            val addressList = geocoder.getFromLocation(
                latitude, longitude, 1
            )
            if (addressList != null && addressList.size > 0) {
                address = addressList[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append("\n")
                }
                val sublocality: String = try {
                    addressCheck(address!!.subLocality.toString())
                } catch (e: Exception) {
                    addressCheck("")
                }
                val localitys = addressCheck(address!!.locality.toString())
                val adminarea = addressCheck(address.adminArea.toString())
                val fea = addressCheck(address.featureName.toString())
                val postalcode = addressCheck(address.postalCode.toString())
                val countryname = addressChecks(address.countryName.toString())
                val opt1s = addressCheckss(opt2.text.toString())
                sb.append(fea)
                sb.append(sublocality)
                sb.append(localitys)
                sb.append(countryname)
                sb.append(adminarea)
                sb.append(opt1s)
                sb.append(opt2.text.toString())
//                sb.append(address.countryName)

                resultss =
                    fea + " " + sublocality + " " + localitys + " " + adminarea + " " + countryname + " " + opt2.text.toString()
//        val s = addressModel(
//          address.latitude.toInt(),
//          address.latitude.toString(),
//          address.longitude.toString(),
////                featureNa,
//          sublocality,
//          localitys,
//          adminarea,
//          postalcode,
//          countryname,
//          "",
//          opt1.text.toString(),
//          opt2.text.toString()
//
//        )
//            sb.append(featureNa).append(", ")
                sb.append(sublocality)
                sb.append(localitys)
                sb.append(adminarea)

                SessionMaintainence.instance!!.lat = address.latitude.toString()
                SessionMaintainence.instance!!.long = address
                    .longitude.toString()
                if (SessionMaintainence.instance!!.addressverify != "") {
                    SessionMaintainence.instance!!.addressverify = resultss
                } else {
                    SessionMaintainence.instance!!.addressverify = ""
                    SessionMaintainence.instance!!.addressverify = resultss
                }
//            val s = addressModel(1, "1/32", "24cross st", "chennai", "chennai", "600048", "chennai")
//        addAddress(s)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Unable connect to Geocoder", e)
        } finally {
            val message = Message.obtain()
            message.target = handler
            if (resultss != null) {
                message.what = 1
                val bundle = Bundle()
                bundle.putString("address", resultss)
                message.data = bundle
            } else {
                resultss = ""
                message.what = 1
                val bundle = Bundle()
                val resd = "Latitude: " + latitude + " Longitude: " + longitude +
                        "\n Unable to get address for this lat-long."
                bundle.putString("address", resultss)
                message.data = bundle
            }
            message.sendToTarget()
        }
        return resultss

    }

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

    override fun onBackPressed() {
//        super.onBackPressed()
        finish()
//        startActivity<MarketActivity>("from" to "map")
    }
    //add address to local
//  fun addAddress(addressModel: addressModel): Boolean {
//    run {
//      try {
//        val reference = reference()
//        val realm = reference.realm()
//        realm.beginTransaction()
//        realm.copyToRealmOrUpdate(addressModel)
//        realm.commitTransaction()
//        return true
//      } catch (e: Exception) {
//        println(e)
//        return false
//      }
//    }
//  }

    //read address from local
//  private fun readAddress(): ArrayList<model> {
//    val reference = reference()
//    val realm = reference.realm()
//    val list = ArrayList<model>()
//    var resultss: RealmResults<addressModel>? = null
//    realm.executeTransaction { realm ->
//      resultss = realm.where(addressModel::class.java).findAll()
//
//      for (addressModel in resultss!!) {
//        val adModel = model()
//        adModel.town = addressModel.town
//        adModel.street = addressModel.street
//        adModel.pincode = addressModel.pincode
//        adModel.state = addressModel.state
//        adModel.door_no = addressModel.door_no
//        adModel.dist = addressModel.dist
//        adModel.lat = addressModel.lat
//        adModel.long = addressModel.longt
//        adModel.id = addressModel.id
//        list.add(adModel)
//      }
//    }
//    return list
//
//
//  }

    companion object {
        private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        val TAG = "MAP LOCATION"
        private val REQUEST_CODE_AUTOCOMPLETE = 1
    }


}