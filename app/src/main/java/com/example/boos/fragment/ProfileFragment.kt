package com.example.boos.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.boos.R
import com.example.boos.Room.GroceryDatabase
import com.example.boos.Room.GroceryRepository
import com.example.boos.Room.GroceryViewModel
import com.example.boos.Room.GroceryViewModelFactory
import com.example.boos.start.Loginpage
import com.example.boos.utili.SessionMaintainence
import com.example.boos.utili.elements
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.profile_bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.profile_fragment.*
import org.jetbrains.anko.startActivity
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
        private const val OPEN_DOCUMENT_CODE = 2
    }

    var imageUri: Uri? = null
    var progress: ProgressDialog? = null
    var firestoreDB: FirebaseFirestore? = null
    lateinit var mWordViewModel: GroceryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        firestoreDB = FirebaseFirestore.getInstance()
        val groceryRepository = GroceryRepository(GroceryDatabase(activity!!))

        val factory = GroceryViewModelFactory(groceryRepository)
        mWordViewModel = ViewModelProviders.of(this, factory).get(GroceryViewModel::class.java)

//        activity!!.equal_navigation_bars.setCurrentActiveItem(2)

        mWordViewModel!!.namess.observe(this, androidx.lifecycle.Observer {
            Glide.with(this)
                .load(SessionMaintainence.instance!!.profilepic)
                .into(profileimage)

        })
        mWordViewModel!!.name.observe(this, androidx.lifecycle.Observer {

            val upperString: String =
                SessionMaintainence.instance!!.fullname!!.substring(0, 1)
                    .toUpperCase() + SessionMaintainence.instance!!.fullname!!.substring(1)
                    .toLowerCase()
            nameuser.text = upperString

        })


        if (SessionMaintainence.instance!!.is_loggedin) {
            profile.visibility = View.VISIBLE
            profilesignup.visibility = View.GONE
            val upperString: String =
                SessionMaintainence.instance!!.fullname!!.substring(0, 1)
                    .toUpperCase() + SessionMaintainence.instance!!.fullname!!.substring(1)
                    .toLowerCase()
            nameuser.text = upperString
            phoneuser.text = "+91 " + SessionMaintainence.instance!!.phoneno
            Glide.with(this)
                .load(SessionMaintainence.instance!!.profilepic)
                .into(profileimage)
            edit.setOnClickListener {
                profilebottomsheet(upperString)
            }
            logout.setOnClickListener {
                val s = elements()
                if (s.isNetworkAvailable(activity!!)) {
                    try {
                        FirebaseMessaging.getInstance()
                            .unsubscribeFromTopic(SessionMaintainence.instance!!.Uid!!)
                            .addOnCompleteListener { task ->
                                var msg = "getString(R.string.msg_subscribed)"
                                if (!task.isSuccessful) {
                                    msg = "getString(R.string.msg_subscribe_failed)"
                                }
                                SessionMaintainence.instance!!.clearSession()
                                mWordViewModel!!.deleteAll()

                                activity!!.startActivity<Loginpage>()
                                activity!!.finish()
                                //                    Log.d(TAG, msg)
                            }
                    } catch (e: Exception) {
                        SessionMaintainence.instance!!.clearSession()
                        mWordViewModel!!.deleteAll()

                        activity!!.startActivity<Loginpage>()
                    }
                } else {
                    SessionMaintainence.instance!!.clearSession()
                    mWordViewModel!!.deleteAll()
                    activity!!.startActivity<Loginpage>()
                }

            }

        } else {
            profile.visibility = View.GONE
            profilesignup.visibility = View.VISIBLE
            buttons.setOnClickListener {
                activity!!.startActivity<Loginpage>()
            }
        }

    }

    private fun profilebottomsheet(fullname: String?) {
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        val view = layoutInflater.inflate(R.layout.profile_bottom_sheet_dialog, null)
        dialog.setContentView(view)
        dialog.dismiss()

//        if (i == 1) {
//            dialog.dismiss()
//            dialog.show()
//        } else if (i == 0) {
//            dialog.dismiss()
//            dialog.show()name_edit_texts
//        }

        dialog.name_edit_text.setText(SessionMaintainence.instance!!.phoneno)
        dialog.inputs1.setText(fullname)
        Glide.with(this)
            .load(SessionMaintainence.instance!!.profilepic)
            .into(dialog.payimages)
        dialog.inputs1.setText(SessionMaintainence.instance!!.fullname)

        mWordViewModel!!.namess.observe(this, androidx.lifecycle.Observer {
            Glide.with(this)
                .load(SessionMaintainence.instance!!.profilepic)
                .into(dialog.payimages)

        })
        mWordViewModel!!.name.observe(this, androidx.lifecycle.Observer {
            dialog.inputs1.setText(SessionMaintainence.instance!!.fullname)

        })


        dialog.buttonas.setOnClickListener {
            if (dialog.inputs1.text.toString() != "") {
                firestoreDB!!.collection("buyer").document(SessionMaintainence.instance!!.Uid!!)
                    .update(
                        mapOf(
                            "name" to dialog.inputs1.text.toString()
                        )
                    )
                    .addOnSuccessListener {
                        SessionMaintainence.instance!!.fullname =
                            dialog.inputs1.text.toString()
                        mWordViewModel!!.insertname(dialog.inputs1.text.toString())

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                imageUri = resultData.data
                val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)
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
        progress = ProgressDialog(activity)
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
                upload()
//                profilebottomsheet(SessionMaintainence.instance!!.lastName, 1)
//                mWordViewModel!!.insertstr(it.toString())
//                mWordViewModel!!.insertstr(it.toString())

//                Glide.with(this)
//                    .load(SessionMaintainence.instance!!.profilepic)
//                    .placeholder(R.drawable.gpay)
//                    .into(dialog.payimages)
            }
        }


    }

    private fun upload() {
        firestoreDB!!.collection("buyer").document(SessionMaintainence.instance!!.Uid!!)
            .update(
                mapOf(
                    "profileimage" to SessionMaintainence.instance!!.profilepic
                )
            )
            .addOnSuccessListener {
                mWordViewModel!!.insertnamess(SessionMaintainence.instance!!.profilepic!!)
                progress!!.dismiss()

            }
            .addOnFailureListener {
                progress!!.dismiss()

            }
    }

}