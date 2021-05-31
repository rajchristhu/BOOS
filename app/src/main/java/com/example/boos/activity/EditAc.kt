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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.ckdroid.geofirequery.setLocation
import com.example.boos.R
import com.example.boos.fragment.HomeFragment
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.startActivity
import java.io.ByteArrayOutputStream

class EditAc : AppCompatActivity() {
    var imageLinks: String = ""
    var image: String = ""
    private val PICK_IMAGE = 100
    var progress: ProgressDialog? = null

    private var imageUri1: Uri? = null
    var imagePath1: String? = ""
    var bitmap: Bitmap? = null
    var firestoreDB: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val name = intent.getStringExtra("name")
        val status = intent.getStringExtra("status")
        val order = intent.getStringExtra("order")
        image = intent.getStringExtra("image")!!
        val id = intent.getStringExtra("id")
        cat_nametxt.setText(name)
        cat_statustxt.setText(status)
        cat_ordertxt.setText(order)
        Glide.with(this)
            .load(image)
            .into(cat_img)
        cat_img.setOnClickListener {
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
        buttonasd.setOnClickListener {
            edits(
                cat_nametxt.text.toString(),
                cat_statustxt.text.toString(),
                cat_ordertxt.text.toString(),
                image,
                id
            )
        }

    }

    private fun edits(
        name: String,
        status: String,
        order: String,
        image: String,
        id: String?
    ) {
        firestoreDB = FirebaseFirestore.getInstance()
        val doc = firestoreDB!!.collection("catekadai").document(id!!)
        doc.update(
            mapOf(
                "cateimage" to image,
                "catename" to name,
                "cateorder" to order,
                "status" to status
            )
        )
            .addOnSuccessListener {
//                doc.setLocation(8.1786, 77.2561, "geo")
//                doc.setLocation(10.7656082, 79.8423888, "geo")
//                doc.setLocation(9.35612, 77.9183, "geo")
                //man
//                doc.setLocation(10.6649, 79.4507, "geo")
                //sattur

                progress!!.dismiss()
                startActivity<UserActivity>()
//                toast("added")
            }
            .addOnFailureListener {
                progress!!.dismiss()
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
            imageUri1 = data!!.data

            imagePath1 = imageUri1.toString()
            cat_img!!.setImageURI(imageUri1)

            bitmap =
                MediaStore.Images.Media.getBitmap(this!!.contentResolver, imageUri1)
            photouploadFile()
        }


    }

    private fun photouploadFile() {
        progress = ProgressDialog(this)
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
                image = ""
                image = it.toString()
                progress!!.dismiss()

            }
        }
    }

}