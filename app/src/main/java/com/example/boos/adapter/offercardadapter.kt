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
import kotlinx.android.synthetic.main.offeradapter.view.*
import org.jetbrains.anko.startActivity

class offercardadapter(val activity: FragmentActivity, val offerList: MutableList<dealModel>) :
    RecyclerView.Adapter<offercardadapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val payimages = itemView.payimages
        val cardView5 = itemView.cardView5
        val jbhkfnjk = itemView.jbhkfnjk

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
    }

    override fun getItemCount(): Int {
        return offerList.size
    }
}