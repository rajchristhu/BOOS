package com.example.boos.adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.boos.R
import com.example.boos.Room.*
import com.example.boos.fragment.HomeFragment
import com.example.boos.utili.SessionMaintainence
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.addca.*
import kotlinx.android.synthetic.main.restitemadapt.view.*
import org.jetbrains.anko.toast
import java.io.ByteArrayOutputStream

class cartadapter(val activity: FragmentActivity, val list: MutableList<GroceryItems>) :
    RecyclerView.Adapter<cartadapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartadapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.restitemadapt, parent, false)
        return ViewHolder(itemView)
    }
    private val PICK_IMAGE = 100
    private var imageUri1: Uri? = null
    var imagePath1: String? = ""
    var bitmap: Bitmap? = null
    var progress: ProgressDialog? = null
    var imageLinkss: String = ""

    lateinit var ViewModel: GroceryViewModel

    var s: Int? = null

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itname = itemView.itname
        val numberPicker = itemView.number_picker
        val desc = itemView.desc
        val butts = itemView.butts
        val view11s = itemView.view11s
        val types = itemView.types
        val itemeImg = itemView.itemeImg
        val prices = itemView.prices
        val textView60 = itemView.textView60
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.itname.text = data.itemName
        holder.prices.text = data.itemQuantity
        holder.desc.text = data.itemPrice.toString() + " ₹"
        val groceryRepository = GroceryRepository(GroceryDatabase(activity!!))
        val factory = GroceryViewModelFactory(groceryRepository)
//        Glide.with(itemActivity)
//            .load(data.image)
//            .placeholder(R.drawable.index)
//            .into(holder.itemeImg)
        // Initialised View Model
        ViewModel = ViewModelProvider(activity, factory).get(GroceryViewModel::class.java)
//        holder.butts.setOnClickListener {
//            val s = list.filter { it.ids.toString() == data.id.toString() }
//            if (s.isNotEmpty()) {
//        holder.butts.visibility = View.INVISIBLE
//        holder.numberPicker.visibility = View.VISIBLE

//                ViewModel.update(s[s.lastIndex].count + 1, data.id.toString())
        holder.numberPicker.number = data.count.toString()

//            else {
//                holder.butts.visibility = View.INVISIBLE
//                holder.numberPicker.visibility = View.VISIBLE
//                val item = GroceryItems(
//                    data.itemName,
//                    data.itemQuantity,
//                    data.id.toString(),
//                    data.mainids,
//                    1,
//                    data.itemPrice.toInt()
//                )
//                holder.numberPicker.number = "1"
//                ViewModel.insert(item)
//            }

//        }

        holder.numberPicker.setOnValueChangeListener(ElegantNumberButton.OnValueChangeListener { view, oldValue, newValue ->
            Log.e("cd", (list.size).toString())

//            if (newValue == 0) {
//                holder.butts.visibility = View.VISIBLE
//                holder.numberPicker.visibility = View.INVISIBLE
//            }
            s = newValue - oldValue
            if (s == 1) {
//                val s = list.filter { it.ids == data.id.toString() }
                ViewModel.update(data.count + 1, data.ids.toString())
            } else {
//                val s = list.filter { it.ids == data.id.toString() }
                if (data.count != 1) {
                    ViewModel.update(data.count  - 1, data.ids.toString())
                } else {
                    ViewModel.deletepar(data.ids.toString())
//                    holder.butts.visibility = View.VISIBLE
//                    holder.numberPicker.visibility = View.INVISIBLE

                }
            }

        })


        ViewModel.allGroceryItems().observe(activity, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            ViewModel.addIssuePost(it)

        })

    }

}