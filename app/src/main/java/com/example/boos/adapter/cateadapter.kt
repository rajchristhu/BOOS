package com.example.boos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boos.R
import com.example.boos.activity.EditAc
import com.example.boos.activity.ItemActivity
import com.example.boos.model.cateModel
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.favadapter.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class cateadapter(
    val activity: FragmentActivity,
    val cateList: MutableList<cateModel>,
    val s: String
) :
    RecyclerView.Adapter<cateadapter.ViewHolder>() {

    var firestoreDB: FirebaseFirestore? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val res = itemView.payname
        val kadaiclick = itemView.kadaiclick
        val view11 = itemView.view11ss
        val payimages = itemView.payimages
        val imageView11 = itemView.imageView11
        val imageView12 = itemView.imageView12
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.favadapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = cateList[position]
        Glide.with(activity)
            .load(data.cateimage)
            .placeholder(R.drawable.index)
            .into(holder.payimages)
        holder.res.text = data.catename
        holder.kadaiclick.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to data.id, "img" to data.cateimage)
        }
        if (SessionMaintainence!!.instance!!.userType == "admin") {
            holder.imageView11.visibility = View.VISIBLE
            holder.imageView12.visibility = View.VISIBLE
        } else {
            holder.imageView11.visibility = View.GONE
            holder.imageView12.visibility = View.GONE
        }
        holder.imageView11.setOnClickListener {
            activity!!.startActivity<EditAc>(
                "name" to data.catename,
                "status" to data.catestatus,
                "order" to data.cateorder,
                "image" to data.cateimage,
                "id" to data.id
            )
        }
        holder.imageView12.setOnClickListener {
            activity!!.alert(
                "Category Name:" + data.catename,
                "Are you sure delete this Category?"
            ) {
                yesButton {
                    firestoreDB = FirebaseFirestore.getInstance()

                    firestoreDB!!.collection("catekadai").document(data.id)
                        .delete()
                        .addOnSuccessListener {
                            activity!!.toast("deleted successfully")
                            activity!!.finish();
                            activity!!.startActivity(activity!!.getIntent());
                        }
                        .addOnFailureListener {
                            activity!!.toast("Failed")
                        }
                }
            }.show()
        }

    }

    override fun getItemCount(): Int {
        return if (cateList.size >= 4) {
            if (s == "2") {
                cateList.size
            } else {
                4
            }

        } else {
            cateList.size
        }

    }
}