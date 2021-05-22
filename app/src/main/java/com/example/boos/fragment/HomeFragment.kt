package com.example.boos.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ckdroid.geofirequery.GeoQuery
import com.ckdroid.geofirequery.model.Distance
import com.ckdroid.geofirequery.setLocation
import com.ckdroid.geofirequery.utils.BoundingBoxUtils
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.boos.R
import com.example.boos.Room.GroceryDatabase
import com.example.boos.Room.GroceryRepository
import com.example.boos.Room.GroceryViewModel
import com.example.boos.Room.GroceryViewModelFactory
import com.example.boos.adapter.*
import com.example.boos.model.cateModel
import com.example.boos.model.dealModel
import com.example.boos.utili.SessionMaintainence
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.addca.*
import kotlinx.android.synthetic.main.adminbottom.*
import kotlinx.android.synthetic.main.adminbottom.buttonas
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.profile_bottom_sheet_dialog.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream


class HomeFragment : Fragment() {
    var imageLinkss: String = ""
    var firestoreDB: FirebaseFirestore? = null
    private val PICK_IMAGE = 100
    private val PICK_IMAGEq = 101
    private val PICK_IMAGEs = 120
    private val PICK_IMAGEss = 1201
    private val PICK_IMAGEsst = 190
    private var imageUri: Uri? = null
    private var imageUri1: Uri? = null
    private var imageUri1q: Uri? = null
    private var imageUri1s: Uri? = null
    private var imageUri1st: Uri? = null
    var imagePath: String? = ""
    var imagePath1: String? = ""
    var imagePath1q: String? = ""
    var imagePath1s: String? = ""
    var imagePath1st: String? = ""
    var imageLinks: String = ""
    var dealList: MutableList<dealModel> = mutableListOf<dealModel>()
    var offerList: MutableList<dealModel> = mutableListOf<dealModel>()
    var trendList: MutableList<dealModel> = mutableListOf<dealModel>()
    var cateList: MutableList<cateModel> = mutableListOf<cateModel>()
    var popList: MutableList<cateModel> = mutableListOf<cateModel>()

    var progress: ProgressDialog? = null
    var bitmap: Bitmap? = null
    var bitmapq: Bitmap? = null
    var bitmapss: Bitmap? = null
    var bitmaps: Bitmap? = null
    var bitmapsst: Bitmap? = null

    companion object {
        private const val OPEN_DOCUMENT_CODE = 2

        fun newInstance() = HomeFragment()
    }

    lateinit var ViewModel: GroceryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shimmer_view_container.visibility = View.VISIBLE
        shimmer_view_container.startShimmerAnimation();
        val groceryRepository = GroceryRepository(GroceryDatabase(activity!!))

        val factory = GroceryViewModelFactory(groceryRepository)
        scr.visibility = View.GONE
        ViewModel = ViewModelProviders.of(this, factory).get(GroceryViewModel::class.java)

        activity!!.bottomNavigation.show(3)
        activity!!.locationtext.visibility=View.VISIBLE
        activity!!.imageView.visibility=View.VISIBLE
        activity!!.textView13.text = "BOSS"

        deal()

        if (SessionMaintainence!!.instance!!.userType == "buyer" || SessionMaintainence!!.instance!!.userType == "") {
            adddeal.visibility = View.GONE
            addoffer.visibility = View.GONE
            addtrends.visibility = View.GONE
            addcate.visibility = View.GONE
            addpopular.visibility = View.GONE
        } else {
            adddeal.visibility = View.VISIBLE
            addoffer.visibility = View.VISIBLE
            addtrends.visibility = View.VISIBLE
            addcate.visibility = View.VISIBLE
            addpopular.visibility = View.VISIBLE
        }


        adddeal.setOnClickListener {
            profilebottomsheet("deal")
        }
        addoffer.setOnClickListener {
            profilebottomsheet("offer")
        }
        addtrends.setOnClickListener {
            profilebottomsheet("trends")
        }
        addcate.setOnClickListener {
            addcates()
        }
        addpopular.setOnClickListener {
            addpop()
        }
//        addtrends.setOnClickListener {
//            addpop()
//        }
        val slideModels: MutableList<SlideModel> = ArrayList()
        slideModels.add(
            SlideModel(
                "https://p.ecopetit.cat/wpic/lpic/26-263518_tumblr-photography-wallpaper-rocks-on-earth-background.jpg",
                "1 Image"
            )
        )
        slideModels.add(
            SlideModel(
                "https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg",
                "2 Image"
            )
        )
        slideModels.add(
            SlideModel(
                "https://live.staticflickr.com/7006/6621416427_8504865e6a_z.jpg",
                "3 Image"
            )
        )
        slideModels.add(
            SlideModel(
                "https://c4.wallpaperflare.com/wallpaper/662/618/496/natur-2560x1600-sceneries-wallpaper-preview.jpg",
                "4 Image"
            )
        )
//        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP)


    }

    private fun addpop() {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.addca, null)
        dialog.setContentView(view)
        dialog.dismiss()
        dialog.show()
        dialog.cat_img.setOnClickListener {
            dialog.dismiss()

            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                dialog.dismiss()

            } else {
//                openAlbum()
                openGalleryspop()
            }
        }
    }

    private fun addcates() {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.addca, null)
        dialog.setContentView(view)
        dialog.dismiss()
        dialog.show()
        dialog.cat_img.setOnClickListener {
            dialog.dismiss()

            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                dialog.dismiss()

            } else {
//                openAlbum()
                openGallerys()
            }
        }
    }

    private fun addBottom(dialog: BottomSheetDialog) {


    }

    fun dpToPixels(dp: Int, context: Context): Float {
        return dp * context.getResources().getDisplayMetrics().density
    }

    private fun profilebottomsheet(s: String) {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.adminbottom, null)
        dialog.setContentView(view)
        dialog.dismiss()
        dialog.imageView6.setOnClickListener {
            dialog.dismiss()

            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                dialog.dismiss()

            } else {
//                openAlbum()
                if (s == "deal") {
                    openGallery()

                } else if (s == "offer") {
                    openGalleryss()

                } else if (s == "trends") {
                    openGallerysst()
                }
            }
        }
        dialog.show()
    }

    private fun openGallerysst() {

        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGEsst)
    }

    private fun profilebottomsheetd(imageUri: Uri?) {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.adminbottom, null)
        dialog.setContentView(view)
        dialog.dismiss()
        dialog.imageView6.setImageURI(imageUri)
        dialog.buttonas.setOnClickListener {
            if (dialog.name_edit_text1.text.toString()
                    .isNullOrEmpty() && dialog.name_edit_text1w.text.toString().isNullOrEmpty()
            ) {
                photouploadFile(
                    dialog.name_edit_text1.text.toString(),
                    dialog.name_edit_text1w.text.toString(),
                    dialog
                )
            } else {
                activity!!.toast("please enter all details")
            }
        }
//
        dialog.imageView6.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
//                openAlbum()
                openGallery()
            }
        }
        dialog.show()
    }

    private fun openGallery() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGEs)
    }

    private fun openGalleryss() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGEss)
    }

    private fun openGallerys() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    private fun openGalleryspop() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGEq)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGEs) {
            imageUri = data!!.data
            imagePath = imageUri.toString()
            profilebottomsheetd(imageUri)
            bitmaps =
                MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)

        }

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri1 = data!!.data
            imagePath1 = imageUri1.toString()
            addbottomsheetd(imageUri1)
            bitmap =
                MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri1)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGEq) {
            imageUri1q = data!!.data
            imagePath1q = imageUri1q.toString()
            addbottomsheetpop(imageUri1q)
            bitmapq =
                MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri1q)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGEss) {
            imageUri1s = data!!.data
            imagePath1s = imageUri1s.toString()
            profilebottomsheetoffer(imageUri1s)
//            addbottomsheetd(imageUri1s)
            bitmapss =
                MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri1s)

        }
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGEsst) {
            imageUri1st = data!!.data
            imagePath1st = imageUri1st.toString()
//            profilebottomsheetoffer(imageUri1st)
            profilebottomsheettrent(imageUri1st)
//            addbottomsheetd(imageUri1s)
            bitmapsst =
                MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri1st)

        }
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // this is the image selected by the user
                val imageUris = data.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUris)
                uploadFile(bitmap, SessionMaintainence.instance!!.Uid!!)
//                val dialog =
//                    BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme) // Style here
//                val view = layoutInflater.inflate(R.layout.profile_bottom_sheet_dialog, null)
//                dialog.setContentView(view)
//                dialog.payimages.setImageURI(imageUri)
            }
        }
    }
    private fun uploadFile(bitmap: Bitmap, userId: String) {
        progress = ProgressDialog(activity!!)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("jobImage")
        val mountainImagesRef = storageRef.child("images/" + userId + "jobImage" + ".jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                SessionMaintainence.instance!!.profilepic = it.toString()
//                profilebottomsheet(SessionMaintainence.instance!!.lastName, 1)
                ViewModel!!.insertstr(it.toString())

//                Glide.with(this)
//                    .load(SessionMaintainence.instance!!.profilepic)
//                    .placeholder(R.drawable.gpay)
//                    .into(dialog.payimages)
                progress!!.dismiss()
            }
        }


    }

    private fun profilebottomsheettrent(imageUri1: Uri?) {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.adminbottom, null)
        dialog.setContentView(view)
        dialog.dismiss()
        dialog.imageView6.setImageURI(imageUri1)
        dialog.buttonas.setOnClickListener {
            if (dialog.name_edit_text1.text.toString()
                    .isNullOrEmpty() && dialog.name_edit_text1w.text.toString().isNullOrEmpty()
            ) {
                activity!!.toast("please enter all details")

            } else {
//                photouploadFileoffer(
//                    dialog.name_edit_text1.text.toString(),
//                    dialog.name_edit_text1w.text.toString(),
//                    dialog
//                ) 
                photouploadFiletrend(
                    dialog.name_edit_text1.text.toString(),
                    dialog.name_edit_text1w.text.toString(),
                    dialog
                )
            }
        }
//
        dialog.imageView6.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
//                openAlbum()
                openGallery()
            }
        }
        dialog.show()

    }

    private fun photouploadFiletrend(name: String, key: String, dialog: BottomSheetDialog) {
        progress = ProgressDialog(activity!!)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("kadai_item")
        val mountainImagesRef =
            storageRef.child(
                "folder" + "/" + SessionMaintainence.instance!!.Uid + Timestamp.now()
                    .toString() + ".jpg"
            )
        val baos = ByteArrayOutputStream()
        bitmapsst!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinks = it.toString()
                progress!!.dismiss()

                addOtrend(name, key, dialog)
//                addOffer(name, key, dialog)
//                addCate(
//                    imageLinks,
//                    topti,
//                    toptitwo,
//                    des, input4, input6
//                )
            }
        }
    }

    private fun addOtrend(name: String, key: String, dialog: BottomSheetDialog) {
        val id = Timestamp.now().toString()
        val model = dealModel(
            id,
            imageLinks,
            key,
            name,
            true
        )
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("trend").document(id)
        doc.set(model)
            .addOnSuccessListener {
//                doc.setLocation(8.1786, 77.2561, "geo")
//                doc.setLocation(10.7656082, 79.8423888, "geo")
//                doc.setLocation(9.35612, 77.9183, "geo")
                //man
//                doc.setLocation(10.6649, 79.4507, "geo")
                //sattur
                doc.setLocation(
                    SessionMaintainence.instance!!.lat!!.toDouble(),
                    SessionMaintainence.instance!!.long!!.toDouble(),
                    "geo"
                )
                progress!!.dismiss()
                dialog.dismiss()
                val intent = activity!!.intent
                activity!!.finish()
                activity!!.startActivity(intent)
//                activity!!.startActivity<DealFormActivity>("name" to name)
//                toast("added")
            }
            .addOnFailureListener {
                progress!!.dismiss()
                dialog.dismiss()

            }

    }

    private fun addbottomsheetpop(imageUri1q: Uri?) {

        val dialogs = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.addca, null)
        dialogs.setContentView(view)
        dialogs.dismiss()
        dialogs.cat_img.setImageURI(imageUri1q)
        dialogs.buttonasd.setOnClickListener {
            if (dialogs.cat_nametxt.text.toString()
                    .isNullOrEmpty() && dialogs.cat_statustxt.text.toString()
                    .isNullOrEmpty() && dialogs.cat_ordertxt.text.toString().isNullOrEmpty()
            ) {
                activity!!.toast("please enter all details")

            } else {
                photouploadFilespop(
                    dialogs.cat_nametxt.text.toString(),
                    dialogs.cat_statustxt.text.toString(),
                    dialogs.cat_ordertxt.text.toString(),
                    dialogs
                )
            }
        }
//
        dialogs.cat_img.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
//                openAlbum()
                openGalleryspop()
            }
        }
        dialogs.show()
    }

    private fun photouploadFilespop(
        name: String,
        status: String,
        order: String,
        dialog: BottomSheetDialog
    ) {
        progress = ProgressDialog(activity!!)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("kadai_item")
        val mountainImagesRef =
            storageRef.child(
                "folder" + "/" + SessionMaintainence.instance!!.Uid + Timestamp.now()
                    .toString() + ".jpg"
            )
        val baos = ByteArrayOutputStream()
        bitmapq!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinkss = it.toString()
                progress!!.dismiss()

                addpopq(name, status, order, dialog)
//                addCate(
//                    imageLinks,
//                    topti,
//                    toptitwo,
//                    des, input4, input6
//                )
            }
        }

    }

    private fun addpopq(
        name: String,
        status: String,
        order: String,
        dialog: BottomSheetDialog
    ) {
        val id = Timestamp.now().toString()
        val model = cateModel(
            id,
            imageLinkss,
            name,
            order,
            status
        )
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("pop").document(id)
        doc.set(model)
            .addOnSuccessListener {
//                doc.setLocation(8.1786, 77.2561, "geo")
//                doc.setLocation(10.7656082, 79.8423888, "geo")
//                doc.setLocation(9.35612, 77.9183, "geo")
                //man
//                doc.setLocation(10.6649, 79.4507, "geo")
                //sattur
                doc.setLocation(
                    SessionMaintainence.instance!!.lat!!.toDouble(),
                    SessionMaintainence.instance!!.long!!.toDouble(),
                    "geo"
                )
                progress!!.dismiss()
                dialog.dismiss()
                val intent = activity!!.intent
                activity!!.finish()
                activity!!.startActivity(intent)
//                activity!!.startActivity<DealFormActivity>("name" to name)
//                toast("added")
            }
            .addOnFailureListener {
                progress!!.dismiss()
                dialog.dismiss()

            }

    }

    private fun profilebottomsheetoffer(imageUri1: Uri?) {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.adminbottom, null)
        dialog.setContentView(view)
        dialog.dismiss()
        dialog.imageView6.setImageURI(imageUri1)
        dialog.buttonas.setOnClickListener {
            if (dialog.name_edit_text1.text.toString()
                    .isNullOrEmpty() && dialog.name_edit_text1w.text.toString().isNullOrEmpty()
            ) {
                activity!!.toast("please enter all details")

            } else {
                photouploadFileoffer(
                    dialog.name_edit_text1.text.toString(),
                    dialog.name_edit_text1w.text.toString(),
                    dialog
                )
            }
        }
//
        dialog.imageView6.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
//                openAlbum()
                openGallery()
            }
        }
        dialog.show()

    }

    fun photouploadFileoffer(name: String, key: String, dialog: BottomSheetDialog) {
        progress = ProgressDialog(activity!!)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("kadai_item")
        val mountainImagesRef =
            storageRef.child(
                "folder" + "/" + SessionMaintainence.instance!!.Uid + Timestamp.now()
                    .toString() + ".jpg"
            )
        val baos = ByteArrayOutputStream()
        bitmapss!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinks = it.toString()
                progress!!.dismiss()

                addOffer(name, key, dialog)
//                addCate(
//                    imageLinks,
//                    topti,
//                    toptitwo,
//                    des, input4, input6
//                )
            }
        }
    }

    fun addOffer(name: String, key: String, dialog: BottomSheetDialog) {
        val id = Timestamp.now().toString()
        val model = dealModel(
            id,
            imageLinks,
            key,
            name,
            true
        )
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("offer").document(id)
        doc.set(model)
            .addOnSuccessListener {
//                doc.setLocation(8.1786, 77.2561, "geo")
//                doc.setLocation(10.7656082, 79.8423888, "geo")
//                doc.setLocation(9.35612, 77.9183, "geo")
                //man
//                doc.setLocation(10.6649, 79.4507, "geo")
                //sattur
                doc.setLocation(
                    SessionMaintainence.instance!!.lat!!.toDouble(),
                    SessionMaintainence.instance!!.long!!.toDouble(),
                    "geo"
                )
                progress!!.dismiss()
                dialog.dismiss()
                val intent = activity!!.intent
                activity!!.finish()
                activity!!.startActivity(intent)
//                activity!!.startActivity<DealFormActivity>("name" to name)
//                toast("added")
            }
            .addOnFailureListener {
                progress!!.dismiss()
                dialog.dismiss()

            }

    }

    private fun addbottomsheetd(imageUri1: Uri?) {
        val dialogs = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.addca, null)
        dialogs.setContentView(view)
        dialogs.dismiss()
        dialogs.cat_img.setImageURI(imageUri1)
        dialogs.buttonasd.setOnClickListener {
            if (dialogs.cat_nametxt.text.toString()
                    .isNullOrEmpty() && dialogs.cat_statustxt.text.toString()
                    .isNullOrEmpty() && dialogs.cat_ordertxt.text.toString().isNullOrEmpty()
            ) {
                activity!!.toast("please enter all details")

            } else {
                photouploadFiles(
                    dialogs.cat_nametxt.text.toString(),
                    dialogs.cat_statustxt.text.toString(),
                    dialogs.cat_ordertxt.text.toString(),
                    dialogs
                )
            }
        }
//
        dialogs.cat_img.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
//                openAlbum()
                openGallery()
            }
        }
        dialogs.show()

    }

    private fun photouploadFiles(
        name: String,
        status: String,
        order: String,
        dialog: BottomSheetDialog
    ) {
        progress = ProgressDialog(activity!!)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("kadai_item")
        val mountainImagesRef =
            storageRef.child(
                "folder" + "/" + SessionMaintainence.instance!!.Uid + Timestamp.now()
                    .toString() + ".jpg"
            )
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinkss = it.toString()
                progress!!.dismiss()

                addcateg(name, status, order, dialog)
//                addCate(
//                    imageLinks,
//                    topti,
//                    toptitwo,
//                    des, input4, input6
//                )
            }
        }

    }

    private fun addcateg(
        name: String,
        status: String,
        order: String,
        dialog: BottomSheetDialog
    ) {
        val id = Timestamp.now().toString()
        val model = cateModel(
            id,
            imageLinkss,
            name,
            order,
            status
        )
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("catekadai").document(id)
        doc.set(model)
            .addOnSuccessListener {
//                doc.setLocation(8.1786, 77.2561, "geo")
//                doc.setLocation(10.7656082, 79.8423888, "geo")
//                doc.setLocation(9.35612, 77.9183, "geo")
                //man
//                doc.setLocation(10.6649, 79.4507, "geo")
                //sattur
                doc.setLocation(
                    SessionMaintainence.instance!!.lat!!.toDouble(),
                    SessionMaintainence.instance!!.long!!.toDouble(),
                    "geo"
                )
                progress!!.dismiss()
                dialog.dismiss()
                val intent = activity!!.intent
                activity!!.finish()
                activity!!.startActivity(intent)
//                activity!!.startActivity<DealFormActivity>("name" to name)
//                toast("added")
            }
            .addOnFailureListener {
                progress!!.dismiss()
                dialog.dismiss()

            }

    }

    private fun photouploadFile(name: String, key: String, dialog: BottomSheetDialog) {
        progress = ProgressDialog(activity!!)
        progress!!.setMessage("Processing..")
        progress!!.setCancelable(false)
        progress!!.show()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("kadai_item")
        val mountainImagesRef =
            storageRef.child(
                "folder" + "/" + SessionMaintainence.instance!!.Uid + Timestamp.now()
                    .toString() + ".jpg"
            )
        val baos = ByteArrayOutputStream()
        bitmaps!!.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {
            progress!!.dismiss()
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                imageLinks = it.toString()
                progress!!.dismiss()

                addDeal(name, key, dialog)
//                addCate(
//                    imageLinks,
//                    topti,
//                    toptitwo,
//                    des, input4, input6
//                )
            }
        }
    }

    private fun addDeal(name: String, key: String, dialog: BottomSheetDialog) {
        val id = Timestamp.now().toString()
        val model = dealModel(
            id,
            imageLinks,
            key,
            name,
            true
        )
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("deal").document(id)
        doc.set(model)
            .addOnSuccessListener {
//                doc.setLocation(8.1786, 77.2561, "geo")
//                doc.setLocation(10.7656082, 79.8423888, "geo")
//                doc.setLocation(9.35612, 77.9183, "geo")
                //man
//                doc.setLocation(10.6649, 79.4507, "geo")
                //sattur
                doc.setLocation(
                    SessionMaintainence.instance!!.lat!!.toDouble(),
                    SessionMaintainence.instance!!.long!!.toDouble(),
                    "geo"
                )
                progress!!.dismiss()
                dialog.dismiss()
                val intent = activity!!.intent
                activity!!.finish()
                activity!!.startActivity(intent)
//                activity!!.startActivity<DealFormActivity>("name" to name)
//                toast("added")
            }
            .addOnFailureListener {
                progress!!.dismiss()
                dialog.dismiss()

            }

    }


    private fun deal() {
        dealList.clear()
        val distanceForRadius = Distance(12.0, BoundingBoxUtils.DistanceUnit.KILOMETERS)
        val db = FirebaseFirestore.getInstance()
        val targetLocation =
            Location("") //provider name is unnecessary

        targetLocation.latitude =
            SessionMaintainence.instance!!.lat!!.toDouble()//your coords of course

        targetLocation.longitude = SessionMaintainence.instance!!.long!!.toDouble()
        val geoQuery = GeoQuery()
            .collection("deal")
//        .whereEqualTo("status","approved")
//        .whereEqualTo("country","IN")
            .whereNearToLocation(targetLocation, distanceForRadius, "geo")
//        .startAfter(lastDocument) //optinal (for pagination)
        geoQuery.get()
            .addOnSuccessListener { addedOrModifiedDataList, removedList ->
                for (i in addedOrModifiedDataList) {
                    val s = i.toObject(dealModel::class.java)
                    dealList.add(s!!)
                }


                val slideModels: MutableList<SlideModel> = ArrayList()

                for (i in dealList) {
                    slideModels.add(SlideModel(i.image, ""))
                }
                activity!!.slider.setImageList(slideModels, scaleType = ScaleTypes.CENTER_CROP)
                caterecs()
//                adapterSet(dealList, kadaiFoodList)
            }
            .addOnFailureListener {
                try {
                    shimmer_view_container.stopShimmerAnimation();

                    shimmer_view_container.visibility = View.GONE
//                    shimmer_view_container.stopShimmerAnimation();
//                    shimmer_view_container.visibility = View.GONE
                    scr.visibility = View.VISIBLE
                } catch (e: Exception) {
                }
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
                    val acceptHorizontalLayoutsss =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    caterec!!.layoutManager = acceptHorizontalLayoutsss
                    caterec!!.adapter = cateadapter(activity!!, cateList)
                } catch (e: Exception) {
                }
                setOffer()
            }
    }

    private fun setOffer() {
        offerList.clear()
        val distanceForRadius = Distance(12.0, BoundingBoxUtils.DistanceUnit.KILOMETERS)
        val db = FirebaseFirestore.getInstance()
        val targetLocation =
            Location("") //provider name is unnecessary

        targetLocation.latitude =
            SessionMaintainence.instance!!.lat!!.toDouble()//your coords of course

        targetLocation.longitude = SessionMaintainence.instance!!.long!!.toDouble()
        val geoQuery = GeoQuery()
            .collection("offer")
//        .whereEqualTo("status","approved")
//        .whereEqualTo("country","IN")
            .whereNearToLocation(targetLocation, distanceForRadius, "geo")
//        .startAfter(lastDocument) //optinal (for pagination)
        geoQuery.get()
            .addOnSuccessListener { addedOrModifiedDataList, removedList ->
                for (i in addedOrModifiedDataList) {
                    val s = i.toObject(dealModel::class.java)
                    offerList.add(s!!)
                }
                try {
                    val acceptHorizontalLayoutsss1 =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)



                    offer!!.layoutManager = acceptHorizontalLayoutsss1
                    offer!!.adapter = offercardadapter(activity!!, offerList)
                } catch (e: Exception) {
                }
                setpop()
//                adapterSet(dealList, kadaiFoodList)
            }
            .addOnFailureListener {
                try {
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmerAnimation();

//                    shimmer_view_container.stopShimmerAnimation();
//                    shimmer_view_container.visibility = View.GONE
                    scr.visibility = View.VISIBLE
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
                    val acceptHorizontalLayoutsss11 =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    caterec1!!.layoutManager = acceptHorizontalLayoutsss11
                    caterec1!!.adapter = papularadapter(activity!!, popList)
                } catch (e: Exception) {
                }
//                setOffer()
                settrend()
            }
    }

    private fun settrend() {
        trendList.clear()
        val distanceForRadius = Distance(12.0, BoundingBoxUtils.DistanceUnit.KILOMETERS)
        val db = FirebaseFirestore.getInstance()
        val targetLocation =
            Location("") //provider name is unnecessary

        targetLocation.latitude =
            SessionMaintainence.instance!!.lat!!.toDouble()//your coords of course

        targetLocation.longitude = SessionMaintainence.instance!!.long!!.toDouble()
        val geoQuery = GeoQuery()
            .collection("trend")
//        .whereEqualTo("status","approved")
//        .whereEqualTo("country","IN")
            .whereNearToLocation(targetLocation, distanceForRadius, "geo")
//        .startAfter(lastDocument) //optinal (for pagination)
        geoQuery.get()
            .addOnSuccessListener { addedOrModifiedDataList, removedList ->
                for (i in addedOrModifiedDataList) {
                    val s = i.toObject(dealModel::class.java)
                    trendList.add(s!!)
                }
                val acceptHorizontalLayoutsss1 =
                    LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)


//
//                val pagerAdapter =
//                    CardFragmentPagerAdapter(
//                        activity!!.supportFragmentManager,
//                        dpToPixels(2, activity!!),trendList
//                    )
//                val fragmentCardShadowTransformer =
//                    ShadowTransformer(activity!!.viewPager, pagerAdapter)
//                fragmentCardShadowTransformer.enableScaling(true)
//
//                activity!!.viewPager.adapter = pagerAdapter
//                activity!!.viewPager.setPageTransformer(false, fragmentCardShadowTransformer)
//                activity!!.viewPager.offscreenPageLimit = 3
                try {
                    viewPager.adapter = MovieAdapter(trendList, activity!!)
                } catch (e: Exception) {
                }
                try {
                    scr.visibility = View.VISIBLE

                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmerAnimation();
                } catch (e: Exception) {
                }
                if (SessionMaintainence.instance!!.is_loggedin) {
                    if (SessionMaintainence.instance!!.fullname == "") {
//                if (!check){
                        profilebottomsheets("", 0)
                    }
//            }
                }
//                adapterSet(dealList, kadaiFoodList)
            }
            .addOnFailureListener {
                try {
                    shimmer_view_container.visibility = View.GONE
                    shimmer_view_container.stopShimmerAnimation();

//                    shimmer_view_container.stopShimmerAnimation();
//                    shimmer_view_container.visibility = View.GONE
                    scr.visibility = View.VISIBLE
                } catch (e: Exception) {
                }
            }

    }

    private fun profilebottomsheets(fullname: String?, i: Int) {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.profile_bottom_sheet_dialog, null)
        dialog.setContentView(view)
        dialog.dismiss()

//        if (i == 1) {
//            dialog.dismiss()
//            dialog.show()
//        } else if (i == 0) {
//            dialog.dismiss()
//            dialog.show()
//        }
        dialog.setCancelable(false)
        dialog.name_edit_text.setText(SessionMaintainence.instance!!.phoneno)
        dialog.inputs1.setText(fullname)
        ViewModel!!.names.observe(this, androidx.lifecycle.Observer {
            Glide.with(this)
                .load(it)
                .into(dialog.payimages)
        })


        dialog.buttonas.setOnClickListener {
            if (dialog.inputs1.text.toString() != "") {
                firestoreDB = FirebaseFirestore.getInstance()
                firestoreDB!!.collection("buyer").document(SessionMaintainence.instance!!.Uid!!)
                    .update(
                        mapOf(
                            "profileimage" to SessionMaintainence.instance!!.profilepic,
                            "name" to dialog.inputs1.text.toString()
                        )
                    )
                    .addOnSuccessListener {
                        SessionMaintainence.instance!!.fullname =
                            dialog.inputs1.text.toString()
                        dialog.dismiss()
                    }
                    .addOnFailureListener {
                        dialog.dismiss()
                    }
            } else {
                dialog.inputs1.error = "Enter the name"
            }
        }
        dialog.payimages.setOnClickListener {
            SessionMaintainence.instance!!.lastName = dialog.inputs1.text.toString()
            selectImageInAlbum(dialog)
        }
        dialog.show()
    }
    fun selectImageInAlbum(dialog: BottomSheetDialog) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, OPEN_DOCUMENT_CODE)
    }
}