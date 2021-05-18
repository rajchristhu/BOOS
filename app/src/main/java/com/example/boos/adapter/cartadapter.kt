package com.example.boos.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.boos.R
import com.example.boos.Room.*
import kotlinx.android.synthetic.main.restitemadapt.view.*

class cartadapter(val activity: FragmentActivity, val list: MutableList<GroceryItems>) :
    RecyclerView.Adapter<cartadapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartadapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.restitemadapt, parent, false)
        return ViewHolder(itemView)
    }

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
        val groceryRepository = GroceryRepository(GroceryDatabase(activity!!))
        val factory = GroceryViewModelFactory(groceryRepository)
//        Glide.with(itemActivity)
//            .load(data.image)
//            .placeholder(R.drawable.index)
//            .into(holder.itemeImg)
        // Initialised View Model
        ViewModel = ViewModelProvider(activity, factory).get(GroceryViewModel::class.java)
        holder.butts.setOnClickListener {
            val s = list.filter { it.ids.toString() == data.id.toString() }
            if (s.isNotEmpty()) {
                holder.butts.visibility = View.INVISIBLE
                holder.numberPicker.visibility = View.VISIBLE

                ViewModel.update(s[s.lastIndex].count + 1, data.id.toString())
                holder.numberPicker.number = (s[s.lastIndex].count + 1).toString()
            } else {
                holder.butts.visibility = View.INVISIBLE
                holder.numberPicker.visibility = View.VISIBLE
                val item = GroceryItems(
                    data.itemName,
                    data.itemQuantity,
                    data.id.toString(),
                    data.mainids,
                    1,
                    data.itemPrice.toInt()
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
                val s = list.filter { it.ids == data.id.toString() }
                ViewModel.update(s[s.lastIndex].count + 1, data.id.toString())
            } else {
                val s = list.filter { it.ids == data.id.toString() }
                if (s[s.lastIndex].count != 0) {
                    ViewModel.update(s[s.lastIndex].count - 1, data.id.toString())
                } else {
                    ViewModel.deletepar(data.id.toString())
                    holder.butts.visibility = View.VISIBLE
                    holder.numberPicker.visibility = View.INVISIBLE

                }
            }

        })


        ViewModel.allGroceryItems().observe(activity, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            val sss = list.filter { it.ids == data.id.toString() }
            ViewModel.addIssuePost(it)

        })

        ViewModel.mIssuePostLiveData.observe(activity, androidx.lifecycle.Observer {
            val ssas = it.filter { it.ids == data.id.toString() }

            if (ssas.size != 0) {
                if (ssas.last().count == 0) {
                    ViewModel.deletepar(data.id.toString())
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
