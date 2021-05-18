package com.example.boos.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ckdroid.geofirequery.model.Distance
import com.ckdroid.geofirequery.utils.BoundingBoxUtils
import com.example.boos.R
import com.example.boos.Room.*
import com.example.boos.adapter.itemadapter
import com.example.boos.model.itemModel
import com.example.boos.utili.SessionMaintainence
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.additem.*
import kotlinx.android.synthetic.main.additem.buttonasd
import kotlinx.android.synthetic.main.additem.cat_img
import kotlinx.android.synthetic.main.additem.cat_nametxt
import kotlinx.android.synthetic.main.additem.cat_ordertxt
import kotlinx.android.synthetic.main.additem.cat_statustxt
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class ItemActivity : AppCompatActivity() {
    private val PICK_IMAGE = 100
    var bitmap: Bitmap? = null
    var dealList: MutableList<itemModel> = mutableListOf<itemModel>()

    private var imageUri: Uri? = null

    var imagePath: String? = ""
    var ids: String? = ""
    var img: String? = ""
    var progress: ProgressDialog? = null
    var firestoreDB: FirebaseFirestore? = null
    lateinit var ViewModel: GroceryViewModel
    val list = mutableListOf<GroceryItems>()

    var imageLinks: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        val groceryRepository = GroceryRepository(GroceryDatabase(this!!))

        val factory = GroceryViewModelFactory(groceryRepository)

        ViewModel = ViewModelProviders.of(this, factory).get(GroceryViewModel::class.java)
        ViewModel.allGroceryItems().observe(this, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            ViewModel.addIssuePost(it)
            if (list.size != 0) {
                textView15.visibility = View.VISIBLE
                textView15.text = list.size.toString()
            } else {
                textView15.visibility = View.GONE

            }
        })




        if (SessionMaintainence!!.instance!!.userType == "buyer" || SessionMaintainence!!.instance!!.userType == "") {
            additem.visibility = View.GONE
        } else {
            additem.visibility = View.VISIBLE
        }
        ids = intent.getStringExtra("id")
        img = intent.getStringExtra("img")
        Glide.with(this)
            .load(img)
            .placeholder(R.drawable.index)
            .into(imageView3)

        reads()
        additem.setOnClickListener {
            addcates()
        }
        imageView7.setOnClickListener {
            finish()
        }
    }

    private fun reads() {
        dealList.clear()
        val distanceForRadius = Distance(12.0, BoundingBoxUtils.DistanceUnit.KILOMETERS)
        val db = FirebaseFirestore.getInstance()

        db.collection("items").document(ids!!).collection("food")
            .get()
            .addOnSuccessListener {
                dealList.clear()
                for (i in it) {
                    val s = i.toObject(itemModel::class.java)
                    dealList.add(s!!)
                }

                val acceptHorizontalLayoutsss11 =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                recyclerViews!!.layoutManager = acceptHorizontalLayoutsss11
                recyclerViews!!.adapter = itemadapter(this!!, dealList)

//                adapterSet(dealList, kadaiFoodList)
            }
            .addOnFailureListener {
                try {
//                    shimmer_view_container.stopShimmerAnimation();
//                    shimmer_view_container.visibility = View.GONE
                    scr.visibility = View.VISIBLE
                } catch (e: Exception) {
                }
            }

    }

    private fun addcates() {
        val dialog = BottomSheetDialog(this!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.additem, null)
        dialog.setContentView(view)
        dialog.dismiss()
        dialog.show()
        dialog.cat_img.setOnClickListener {
            dialog.dismiss()

            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    this!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this!!,
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

    private fun openGallerys() {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data!!.data
            imagePath = imageUri.toString()
            addbottomsheetd(imageUri)
            bitmap =
                MediaStore.Images.Media.getBitmap(this!!.contentResolver, imageUri)
        }


    }

    private fun addbottomsheetd(imageUri1: Uri?) {
        val dialogs = BottomSheetDialog(this!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.additem, null)
        dialogs.setContentView(view)
        dialogs.dismiss()
        dialogs.cat_img.setImageURI(imageUri1)
        dialogs.buttonasd.setOnClickListener {
            if (dialogs.cat_nametxt.text.toString()
                    .isNullOrEmpty() && dialogs.cat_statustxt.text.toString()
                    .isNullOrEmpty() && dialogs.cat_ordertxt.text.toString().isNullOrEmpty()
            ) {
                toast("please enter all details")

            } else {
                photouploadFiles(
                    dialogs.cat_nametxt.text.toString(),
                    dialogs.cat_statustxt.text.toString(),
                    dialogs.cat_ordertxt.text.toString(),
                    dialogs.item_stacks.text.toString(),
                    dialogs.quans.text.toString(),
                    dialogs
                )
            }
        }
//
        dialogs.cat_img.setOnClickListener {
            val checkSelfPermission =
                ContextCompat.checkSelfPermission(
                    this!!,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this!!,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
//                openAlbum()
                openGallerys()
            }
        }
        dialogs.show()

    }

    private fun photouploadFiles(
        cat_nametxt: String,
        oripr: String,
        itempri: String,
        stack: String,
        quan: String,
        dialogs: BottomSheetDialog
    ) {
        progress = ProgressDialog(this!!)
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
                imageLinks = it.toString()
                progress!!.dismiss()

                addcItem(cat_nametxt, oripr, itempri, stack, quan, dialogs)
//                addCate(
//                    imageLinks,
//                    topti,
//                    toptitwo,
//                    des, input4, input6
//                )
            }
        }
    }

    private fun addcItem(
        catNametxt: String,
        oripr: String,
        itempri: String,
        stack: String,
        quan: String,
        dialogs: BottomSheetDialog
    ) {
        val id = SessionMaintainence.instance!!.Uid + Timestamp.now().toString() + catNametxt
        val model = itemModel(
            id,
            ids!!,
            imageLinks,
            catNametxt,
            oripr,
            quan,
            stack,
            itempri
        )
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("items").document(ids!!).collection("food")
        doc.add(model)
            .addOnSuccessListener {
                progress!!.dismiss()
                dialogs.dismiss()
                val intent = this!!.intent
                this!!.finish()
                this!!.startActivity(intent)
//                activity!!.startActivity<DealFormActivity>("name" to name)
//                toast("added")
            }
            .addOnFailureListener {
                progress!!.dismiss()
                dialogs.dismiss()

            }


    }

}