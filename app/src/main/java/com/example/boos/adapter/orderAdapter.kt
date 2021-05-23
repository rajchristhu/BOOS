package com.example.boos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boos.R

import com.example.boos.model.orederModel
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.orderadapter.view.*
import java.text.SimpleDateFormat
import java.util.*

class orderAdapter(
    val orderList: MutableList<orederModel>,
    val context: Context
) : RecyclerView.Adapter<orderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): orderAdapter.ViewHolder {
        return orderAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.orderadapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: orderAdapter.ViewHolder, position: Int) {
        val data = orderList[position]
        holder.shopname.text = data.shopName
        holder.price.text = data.totalPrice+" ₹"
        val sfd = getcurrentDateTime(data.postedTime!!)
//        sfd.format(Date(data.postedTime.toString()))
        holder.date.text = sfd.toString()
        var re = arrayListOf<String>()
        for (i in data.foodItems!!) {
            re.add(i.foodName + " × " + i.count + ", ")

        }
        val stringArray = re. map { it. toString() }. toTypedArray()

        holder.item.text = re.joinToString()
        holder.delpar.text = data.deleveryperson
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shopname = itemView.shopname
        val price = itemView.price
        val date = itemView.date
        val item = itemView.item
        val delpar = itemView.delpar
    }

    private fun getcurrentDateTime(datee: Timestamp): String? {
        val milliseconds = datee.seconds * 1000 + datee.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd-MMMM-yyyy")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate)
        return date
    }

}