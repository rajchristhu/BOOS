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
import kotlinx.android.synthetic.main.papularadapter.view.*
import org.jetbrains.anko.startActivity

class papularadapter(
    val activity: FragmentActivity,
    private val slideModels: MutableList<cateModel>,
    val s: String
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
        val data = slideModels[position]
        Glide.with(activity)
            .load(data.cateimage)
            .placeholder(R.drawable.index)
            .into(holder.payimages)
        holder.kadaiclick.setOnClickListener {
            activity!!.startActivity<ItemActivity>("id" to data.id, "img" to data.cateimage)
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