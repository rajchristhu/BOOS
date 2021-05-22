package com.example.boos.adapter

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.boos.R
import com.example.boos.Room.*
import com.example.boos.activity.ItemActivity
import com.example.boos.model.itemModel
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.restitemadapt.view.*

class itemadapter(val itemActivity: ItemActivity, val dealList: MutableList<itemModel>) :
    RecyclerView.Adapter<itemadapter.ViewHolder>() {
    val list = mutableListOf<GroceryItems>()
    var s: Int? = null

    lateinit var ViewModel: GroceryViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemadapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.restitemadapt, parent, false)
        return ViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return dealList.size
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
        val data = dealList[position]
        holder.itname.text = data.name
        holder.prices.text = data.quan
        holder.desc.text = data.itempri + " ₹"
        holder.textView60.visibility = View.VISIBLE
        holder.textView60.text = data.oripri + " ₹"
        holder.textView60.paintFlags = holder.textView60.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        val groceryRepository = GroceryRepository(GroceryDatabase(itemActivity!!))
        val factory = GroceryViewModelFactory(groceryRepository)
        Glide.with(itemActivity)
            .load(data.image)
            .placeholder(R.drawable.index)
            .into(holder.itemeImg)
        // Initialised View Model
        ViewModel = ViewModelProvider(itemActivity, factory).get(GroceryViewModel::class.java)
        holder.butts.setOnClickListener {
            val s = list.filter { it.ids.toString() == data.id }
            if (s.isNotEmpty()) {
                holder.butts.visibility = View.INVISIBLE
                holder.numberPicker.visibility = View.VISIBLE
                val items = GroceryItems(
                    data.name,
                    data.quan,
                    data.id,
                    data.itemid,
                    s[s.lastIndex].count + 1,
                    data.itempri.toInt()
                )
                ViewModel.update(s[s.lastIndex].count + 1, data.id)
                holder.numberPicker.number = (s[s.lastIndex].count + 1).toString()
            } else {
                holder.butts.visibility = View.INVISIBLE
                holder.numberPicker.visibility = View.VISIBLE
                val item = GroceryItems(
                    data.name,
                    data.quan,
                    data.id,
                    data.itemid,
                    1,
                    data.itempri.toInt()
                )
                holder.numberPicker.number = "1"
                ViewModel.insert(item)
            }

        }

        holder.numberPicker.setOnValueChangeListener(ElegantNumberButton.OnValueChangeListener { view, oldValue, newValue ->
            Log.e("cd", (list.size).toString())

            if (newValue == 0) {
                holder.butts.visibility = View.VISIBLE
                holder.numberPicker.visibility = View.INVISIBLE
            }
            s = newValue - oldValue
            if (s == 1) {
                val s = list.filter { it.ids == data.id }
                ViewModel.update(s[s.lastIndex].count + 1, data.id)
            } else {
                val s = list.filter { it.ids == data.id }
                if (s[s.lastIndex].count != 0) {
                    ViewModel.update(s[s.lastIndex].count - 1, data.id)
                } else {
                    ViewModel.deletepar(data.id)
                    holder.butts.visibility = View.VISIBLE
                    holder.numberPicker.visibility = View.INVISIBLE

                }
            }

        })


        ViewModel.allGroceryItems().observe(itemActivity, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            val sss = list.filter { it.ids == data.id }
            ViewModel.addIssuePost(it)

        })

        ViewModel.mIssuePostLiveData.observe(itemActivity, androidx.lifecycle.Observer {
            val ssas = it.filter { it.ids == data.id }

            if (ssas.size != 0) {
                if (ssas.last().count == 0) {
                    ViewModel.deletepar(data.id)
                    holder.butts.visibility = View.VISIBLE
                    holder.numberPicker.visibility = View.INVISIBLE
                } else {
                    holder.butts.visibility = View.INVISIBLE
                    holder.numberPicker.visibility = View.VISIBLE
                    holder.numberPicker.number = ssas.last().count.toString()
                }

            } else {
                holder.butts.visibility = View.VISIBLE
                holder.numberPicker.visibility = View.INVISIBLE
            }
        })


    }
}