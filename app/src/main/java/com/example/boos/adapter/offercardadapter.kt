package com.example.boos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boos.R
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.offeradapter.view.*

class offercardadapter( val activity: FragmentActivity) :
    RecyclerView.Adapter<offercardadapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val payimages = itemView.payimages

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): offercardadapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.offeradapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: offercardadapter.ViewHolder, position: Int) {
        Glide.with(activity)
            .load(R.drawable.yui)
            .placeholder(R.drawable.index)
            .into(holder.payimages)
    }

    override fun getItemCount(): Int {
        return 5
    }
}