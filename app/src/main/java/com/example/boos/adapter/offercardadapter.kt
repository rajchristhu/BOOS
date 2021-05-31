package com.example.boos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.FragmentActivity

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boos.R
import com.example.boos.activity.ItemActivity
import com.example.boos.model.dealModel
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.offeradapter.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class offercardadapter(val activity: FragmentActivity, val offerList: MutableList<dealModel>) :
    RecyclerView.Adapter<offercardadapter.ViewHolder>() {
    var firestoreDB: FirebaseFirestore? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val payimages = itemView.payimages
        val cardView5 = itemView.cardView5
        val jbhkfnjk = itemView.jbhkfnjk
        val imageView13 = itemView.imageView13

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): offercardadapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.offeradapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: offercardadapter.ViewHolder, position: Int) {
        val data = offerList[position]
        Glide.with(activity)
            .load(data.image)
            .placeholder(R.drawable.index)
            .into(holder.payimages)
        holder.cardView5.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to data.id, "img" to data.image)
        }
        holder.cardView5.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to data.id, "img" to data.image)
        }
        holder.jbhkfnjk.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to data.id, "img" to data.image)
        }
        if (SessionMaintainence!!.instance!!.userType == "admin") {
            holder.imageView13.visibility = View.VISIBLE
//            holder.imageView12.visibility = View.VISIBLE
        } else {
            holder.imageView13.visibility = View.GONE
//            holder.imageView12.visibility = View.GONE
        }
        holder.imageView13.setOnClickListener {
            activity!!.alert("Offer Name:" + data.name, "Are you sure delete this Offer?") {
                yesButton {
                    firestoreDB = FirebaseFirestore.getInstance()

                    firestoreDB!!.collection("offer").document(data.id)
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
        return offerList.size
    }
}