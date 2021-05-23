package com.example.boos.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.boos.R
import com.example.boos.activity.DelsessionActivity
import com.example.boos.model.orederModel
import com.example.boos.utili.SessionMaintainence

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_delsession.*
import kotlinx.android.synthetic.main.payadapt.view.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import java.text.SimpleDateFormat
import java.util.*

class deliveryadapters
    (
    val paymentDetails: Activity,
    val mutableList: MutableList<orederModel>
) :
    RecyclerView.Adapter<deliveryadapters.ViewHolder>() {
    var firestoreDB: FirebaseFirestore? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): deliveryadapters.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(
                R.layout.payadapt, parent, false
            )
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    override fun onBindViewHolder(holder: deliveryadapters.ViewHolder, position: Int) {
        val data = mutableList[position]
        holder.name.text = data.userName
        holder.textView76.text = "Address"
        firestoreDB = FirebaseFirestore.getInstance()
        holder.shoptitle.visibility = View.VISIBLE
        holder.shopnames.visibility = View.VISIBLE
        holder.landtitle.visibility = View.VISIBLE
        holder.lanmark.visibility = View.VISIBLE
        holder.shopnames.text = data.shopName
        holder.lanmark.text = data.landmark


        when (data.status) {
            "close" -> {
                holder.give2.visibility = View.GONE
                holder.phone.visibility = View.GONE
//                holder.give2.visibility = View.GONE
                holder.button7.visibility = View.GONE

                holder.give.text = mutableList.size.toString()
                holder.give.setBackgroundResource(R.drawable.beforebutton)
                holder.give.setTextColor(paymentDetails.resources.getColor(R.color.grays))
            }
            "open" -> {
                holder.give.text = "Complete Order"
                holder.button7.visibility = View.GONE

                holder.button7.text = "cancel"

                holder.button7.setOnClickListener {
                    paymentDetails.alert(
                        "Delete this order",
                        "Delete"
                    ) {
                        yesButton { its ->

                            firestoreDB!!.collection("order").document(data.orderId)
                                .delete()
                                .addOnSuccessListener {
                                    paymentDetails.toast("deleted")

                                }
                                .addOnFailureListener {

                                }
                        }
                    }.show()
                }
                holder.give2.visibility = View.VISIBLE
                holder.give.visibility = View.GONE
                holder.give2.setOnClickListener {
                    paymentDetails.alert(
                        "You pickup this order? ",
                        "Pickup Order"
                    ) {
                        yesButton { its ->
                            firestoreDB!!.collection("order").document(data.orderId)
                                .update(
                                    mapOf(
                                        "status" to "onway",
                                        "deleveryperson" to SessionMaintainence.instance!!.fullname,
                                        "deleverypersonPhoneno" to SessionMaintainence.instance!!.phoneno,
                                        "deliId" to SessionMaintainence.instance!!.Uid
                                    )
                                )
                                .addOnSuccessListener {
                                    its.dismiss()

                                    paymentDetails.startActivity<DelsessionActivity>()
                                    paymentDetails.finish()
                                }
                                .addOnFailureListener {
                                    its.dismiss()
                                }
                        }

                    }.show()
                }

                holder.give.setOnClickListener {


                    paymentDetails.alert(
                        "Can you finish this order ",
                        "Complete Order"
                    ) {
                        yesButton { its ->
                            firestoreDB!!.collection("order").document(data.orderId)
                                .update(
                                    mapOf(
                                        "status" to "close",
                                        "deleveryperson" to SessionMaintainence.instance!!.fullname,
                                        "deleverypersonPhoneno" to SessionMaintainence.instance!!.phoneno,
                                        "deliId" to SessionMaintainence.instance!!.Uid
                                    )
                                )
                                .addOnSuccessListener {
                                    its.dismiss()

                                    paymentDetails.startActivity<DelsessionActivity>()
                                    paymentDetails.finish()
                                }
                                .addOnFailureListener {
                                    its.dismiss()
                                }
                        }

                    }.show()
                }
            }
            else -> {
                holder.button7.visibility = View.GONE

                holder.button7.text = "cancel"

                holder.button7.setOnClickListener {
                    paymentDetails.alert(
                        "Delete this order",
                        "Delete"
                    ) {
                        yesButton { its ->

                            firestoreDB!!.collection("order").document(data.orderId)
                                .delete()
                                .addOnSuccessListener {
                                    paymentDetails.toast("deleted")

                                }
                                .addOnFailureListener {

                                }
                        }
                    }.show()
                }

                holder.give.text = "Complete Order"
                holder.give2.visibility = View.GONE
                holder.give.visibility = View.VISIBLE
                holder.give2.setOnClickListener {
                    paymentDetails.alert(
                        "You pickup this order? ",
                        "Pickup Order"
                    ) {
                        yesButton { its ->
                            firestoreDB!!.collection("order").document(data.orderId)
                                .update(
                                    mapOf(
                                        "status" to "onway",
                                        "deleveryperson" to SessionMaintainence.instance!!.fullname,
                                        "deleverypersonPhoneno" to SessionMaintainence.instance!!.phoneno,
                                        "deliId" to SessionMaintainence.instance!!.Uid
                                    )
                                )
                                .addOnSuccessListener {
                                    its.dismiss()

                                    paymentDetails.startActivity<DelsessionActivity>()
                                    paymentDetails.finish()
                                }
                                .addOnFailureListener {
                                    its.dismiss()
                                }
                        }

                    }.show()
                }

                holder.give.setOnClickListener {
                    paymentDetails.alert(
                        "Can you finish this order ",
                        "Complete Order"
                    ) {
                        yesButton { its ->
                            firestoreDB!!.collection("order").document(data.orderId)
                                .update(
                                    mapOf(
                                        "status" to "close",
                                        "deleveryperson" to SessionMaintainence.instance!!.fullname,
                                        "deleverypersonPhoneno" to SessionMaintainence.instance!!.phoneno,
                                        "deliId" to SessionMaintainence.instance!!.Uid
                                    )
                                )
                                .addOnSuccessListener {
                                    its.dismiss()

                                    paymentDetails.startActivity<DelsessionActivity>()
                                    paymentDetails.finish()
                                }
                                .addOnFailureListener {
                                    its.dismiss()
                                }
                        }

                    }.show()
                }
            }
        }

        var sw = ""
        var oi = 0
        for (i in data.foodItems!!) {
            sw += ", " + i.foodName + " × " + i.count
            val po = i.totalPrice.toInt()
            oi += po
        }
        holder.add.text = sw
//        holder.pricew.text =  (oi+10).toString()
        holder.pricew.text = (data.totalPrice.toInt()).toString() + " (" + data.paymentMethod + ")"

        var opsw = 0
        for (i in mutableList)
        {
            opsw+=i.totalPrice.toInt()
        }
        paymentDetails.textView92.text =opsw.toString()

        if (data.status != "close") {
            if (data.userpho == "") {
                holder.phone.visibility = View.INVISIBLE
            } else {
                holder.phone.visibility = View.VISIBLE
            }
        }
        holder.phone.setOnClickListener {
            val phon = data.userpho
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phon")
            paymentDetails.startActivity(intent)
        }
//        val date = getcurrentDateTime(data.postedTime!!)
        val date = getcurrentDateTime(data.postedTime!!)
        holder.textView87.visibility = View.VISIBLE
        holder.textView86.visibility = View.VISIBLE
        holder.textView87.text = date
        holder.textView97.text = data.deleveryperson
        holder.textView78.text = data.address
        holder.map.setOnClickListener {
            startGoogleMaps(paymentDetails, data.lat.toDouble(), data.long.toDouble())
        }
//        holder.map.visibility = View.GONE
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.name
        val add = itemView.add
        val phone = itemView.phone
        val map = itemView.map
        val full = itemView.full
        val pricew = itemView.textView69
        val textView78 = itemView.textView78
        val textView87 = itemView.textView87
        val textView86 = itemView.textView86
        val textView97 = itemView.textView97
        val textView76 = itemView.textView76
        val give = itemView.give
        val shoptitle = itemView.shoptitle
        val shopnames = itemView.shopname
        val landtitle = itemView.landtitle
        val give2 = itemView.give2
        val lanmark = itemView.lanmark
        val button7 = itemView.button7

    }

    private fun getcurrentDateTime(datee: Timestamp): String? {
        val milliseconds = datee.seconds * 1000 + datee.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd-MMMM-yyyy, HH:mm:ss")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate)
        return date
    }

    fun startGoogleMaps(context: Context, lat: Double, long: Double) {
        startWebBrowser(
            context,
            Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$long")
        )
    }

    fun startWebBrowser(context: Context, link: Uri?) {
        if (link != null) {
            val webIntent = Intent(Intent.ACTION_VIEW, link).apply {
                addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            if (webIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(webIntent)
            }
        }
    }

}