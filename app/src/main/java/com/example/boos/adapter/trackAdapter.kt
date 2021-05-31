package com.example.boos.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boos.R
import com.example.boos.model.orederModel
import com.google.firebase.Timestamp
import kotlinx.android.synthetic.main.trackadapter.view.*
import java.text.SimpleDateFormat
import java.util.*

class trackAdapter(
    val mutableList: MutableList<orederModel>,
    val context: Context
) : RecyclerView.Adapter<trackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): trackAdapter.ViewHolder {
        return trackAdapter.ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.trackadapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: trackAdapter.ViewHolder, position: Int) {
        val data = mutableList[position]
        if (data.status == "open") {
            holder.shopname.text = "Preparing"
            holder.image.setAnimation("pkgsaw.json")

            holder.image.playAnimation()
            holder.image.loop(true)
        } else {
            holder.shopname.text = "On the way"
            holder.image.setAnimation("delguy.json")

            holder.image.playAnimation()
            holder.image.loop(true)

        }
        if (data.deleverypersonPhoneno == "") {
            holder.imageView10.visibility = View.INVISIBLE
            holder.textView55.visibility = View.INVISIBLE
        } else {
            holder.imageView10.visibility = View.VISIBLE
            holder.textView55.visibility = View.VISIBLE
        }
        holder.price.text = (data.totalPrice.toInt()).toString() + " ₹"
        val sfd = getcurrentDateTime(data.postedTime!!)
//        sfd.format(Date(data.postedTime.toString()))
        holder.date.text = sfd.toString()
        holder.imageView10.setOnClickListener {
            val phon = data.deleverypersonPhoneno
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phon")
            context.startActivity(intent)
        }
        var re = arrayListOf<String>()
        for (i in data.foodItems!!) {
            re.add(i.foodName + " × " + i.count + ", ")

        }
        val stringArray = re.map { it.toString() }.toTypedArray()

        holder.item.text = re.joinToString()
        holder.delpar.text = data.deleveryperson
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val shopname = itemView.shopnames
        val price = itemView.price
        val date = itemView.date
        val item = itemView.item
        val delpar = itemView.delpar
        val image = itemView.image
        val imageView10 = itemView.imageView10
        val textView55 = itemView.textView55
    }

    private fun getcurrentDateTime(datee: Timestamp): String? {
        val milliseconds = datee.seconds * 1000 + datee.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd-MMMM-yyyy")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate)
        return date
    }
}