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
import kotlinx.android.synthetic.main.favadapter.view.*
import org.jetbrains.anko.startActivity

class cateadapter(
    val activity: FragmentActivity,
    val cateList: MutableList<cateModel>
) :
    RecyclerView.Adapter<cateadapter.ViewHolder>() {


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val res = itemView.payname
        val kadaiclick = itemView.kadaiclick
        val view11 = itemView.view11ss
        val payimages = itemView.payimages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.favadapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=cateList[position]
        Glide.with(activity)
            .load(data.cateimage)
            .placeholder(R.drawable.index)
            .into(holder.payimages)
        holder.res.text=data.catename
        holder.kadaiclick.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to data.id, "img" to data.cateimage)
        }
    }

    override fun getItemCount(): Int {
        return if (cateList.size >= 5) {
            5
        } else {
            cateList.size
        }

    }
}