package com.example.boos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.models.SlideModel
import com.example.boos.R
import kotlinx.android.synthetic.main.papularadapter.view.*

class papularadapter(
    val activity: FragmentActivity,
    private val slideModels: MutableList<SlideModel>
) :
    RecyclerView.Adapter<papularadapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val res = itemView.payname
        val kadaiclick = itemView.kadaiclick
        val view11 = itemView.view11ss
        val payimages = itemView.payimages
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.papularadapter, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(activity)
            .load(slideModels[position].imageUrl)
            .placeholder(R.drawable.index)
            .into(holder.payimages)
    }

    override fun getItemCount(): Int {
       return slideModels.size
    }
}