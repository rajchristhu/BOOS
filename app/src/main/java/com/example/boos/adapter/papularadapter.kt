package com.example.boos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boos.R
import com.example.boos.activity.ItemActivity
import com.example.boos.model.cateModel
import com.example.boos.utili.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.papularadapter.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton

class papularadapter(
    val activity: FragmentActivity,
    private val slideModels: MutableList<cateModel>,
    val s: String
) :
    RecyclerView.Adapter<papularadapter.ViewHolder>() {
    var firestoreDB: FirebaseFirestore? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val res = itemView.payname
        val kadaiclick = itemView.kadaiclick
        val view11 = itemView.view11ss
        val payimages = itemView.payimages
        val imageView15 = itemView.imageView15
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.papularadapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = slideModels[position]
        Glide.with(activity)
            .load(data.cateimage)
            .placeholder(R.drawable.index)
            .into(holder.payimages)
        holder.res.text = data.catename
        holder.kadaiclick.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to data.id, "img" to data.cateimage)
        }
        if (SessionMaintainence!!.instance!!.userType == "admin") {
            holder.imageView15.visibility = View.VISIBLE
//            holder.imageView12.visibility = View.VISIBLE
        } else {
            holder.imageView15.visibility = View.GONE
//            holder.imageView12.visibility = View.GONE
        }
        holder.imageView15.setOnClickListener {
            activity!!.alert(" Name:" + data.catename, "Are you sure delete this Name?") {
                yesButton {
                    firestoreDB = FirebaseFirestore.getInstance()

                    firestoreDB!!.collection("pop").document(data.id)
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
        return if (slideModels.size >= 4) {
            if (s == "2") {
                slideModels.size
            } else {
                4
            }

        } else {
            slideModels.size
        }
    }
}