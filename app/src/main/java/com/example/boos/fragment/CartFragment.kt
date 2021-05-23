package com.example.boos.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.boos.R
import com.example.boos.Room.*
import com.example.boos.adapter.cartadapter
import com.example.boos.model.orderfoodModel
import com.example.boos.model.orederModel
import com.example.boos.util.dialog
import com.example.boos.util.dialogew
import com.example.boos.util.dialogewd
import com.example.boos.utili.SessionMaintainence
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_item.*
import kotlinx.android.synthetic.main.activity_item.constraintLayout3
import kotlinx.android.synthetic.main.activity_item.recyclerViews
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.cart_fragment.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.toast

class CartFragment : Fragment() {
    lateinit var ViewModel: GroceryViewModel
    val list = mutableListOf<GroceryItems>()

    companion object {
        fun newInstance() = CartFragment()
    }

    var firestoreDB: FirebaseFirestore? = null
    lateinit var mWordViewModel: GroceryViewModel

    private lateinit var viewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cart_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val groceryRepository = GroceryRepository(GroceryDatabase(activity!!))
        itemcards.setOnClickListener {
            if (SessionMaintainence.instance!!.is_loggedin) {
                bottomSheetshow(list)
            } else {
                showDialog()
            }
        }

        firestoreDB = FirebaseFirestore.getInstance()

        activity!!.bottomNavigation.show(4)
        activity!!.locationtext.visibility = View.GONE
        activity!!.imageView.visibility = View.GONE
        activity!!.textView13.text = "My Cart"
        val factory = GroceryViewModelFactory(groceryRepository)
        if (list.size != 0) {
            constraintLayout3.visibility = View.VISIBLE
            image.visibility = View.GONE
            textView16.visibility = View.GONE
        } else {
            constraintLayout3.visibility = View.GONE
            image.visibility = View.VISIBLE
            textView16.visibility = View.VISIBLE
            image.setAnimation("emca.json")
            image.playAnimation()
            image.loop(true)
        }
        ViewModel = ViewModelProviders.of(this, factory).get(GroceryViewModel::class.java)
        ViewModel.allGroceryItems().observe(this, androidx.lifecycle.Observer {
            list.clear()
            list.addAll(it)
            ViewModel.addIssuePost(it)
            if (list.size != 0) {
                itemcards.visibility = View.VISIBLE
                if (list.size <= 1) {
                    itemcounts.text = list.size.toString() + " ITEM"

                } else {
                    itemcounts.text = list.size.toString() + " ITEMS"

                }
                var price = 0
                for (i in list) {
                    price += i.itemPrice.toInt()*i.count
                }
                priceitems.text = "$price ₹"
            } else {
                itemcards.visibility = View.GONE
            }


            if (list.size != 0) {
                constraintLayout3.visibility = View.VISIBLE
                image.visibility = View.GONE
                textView16.visibility = View.GONE
                val acceptHorizontalLayoutsss11 =
                    LinearLayoutManager(activity!!, LinearLayoutManager.VERTICAL, false)
                recyclerViews!!.layoutManager = acceptHorizontalLayoutsss11
                recyclerViews!!.adapter = cartadapter(activity!!, list)
                recyclerViews!!.adapter!!.notifyDataSetChanged()
            } else {
                constraintLayout3.visibility = View.GONE
                image.visibility = View.VISIBLE
                textView16.visibility = View.VISIBLE
                image.setAnimation("emca.json")
                image.playAnimation()
                image.loop(true)
            }
        })
    }

    private fun bottomSheetshow(list: MutableList<GroceryItems>) {
        val view = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        val dialog = BottomSheetDialog(activity!!, R.style.AppBottomSheetDialogTheme) // Style here
        dialog.setContentView(view)
        dialog.name_edit_text.setText(SessionMaintainence.instance!!.addressverify)
        var price = 0
        for (i in list) {
            price += i.itemPrice.toInt() * i.count
        }
        dialog.price.text = "$price ₹"
//            dialog.date.text = traderBidmodel.scheduling_time
//            dialog.price.text = "₹" +"21"
//            dialog.day.text = "fs"

        dialog.s.setOnClickListener {
            val sch = listOf("₹ Cash on delivery")
            activity!!.selector("Choose payment method", sch) { dialogInterface, i ->
                dialog.textView30.text = sch[i]

                if (i == 0) {
                    Glide.with(this!!)
                        .load(R.drawable.place)
                        .into(dialog.payimages)
                } else if (i == 1
                ) {
                    Glide.with(this!!)
                        .load(R.drawable.place)
                        .into(dialog.payimages)
                }
            }
        }
        dialog.placeo.setOnClickListener {
            val s = price

            if (dialog.inputs1.text.toString().isEmpty()) {
                dialog.inputs1.error = "Enter the landmark"
            } else {
                if (dialog.textView30.text.toString() == "Choose payment method >") {
                    activity!!.toast("choose payment")
                } else {
                    if (s.toInt() >= 100) {
//                            swe = dialog.inputs1.text.toString()
//                            sse = dialog.name_edit_text.text.toString()
                        dialog.dismiss()

                        payment(
                            dialog.textView30.text.toString(),
                            price,
                            list,
                            price,
                            "1",
                            dialog.inputs1.text.toString(),
                            dialog.name_edit_text.text.toString()
                        )
                    } else {
                        dialog.dismiss()
                        showDialogs()
                    }
                }
            }

        }
        dialog.show()

    }

    private fun payment(
        meth: String,
        price: Int,
        list: MutableList<GroceryItems>,
        price1: Int,
        orgi: String,
        land: String,
        add: String
    ) {

        if (meth == "₹ Cash on delivery") {
            val ed = arrayListOf<orderfoodModel>()
            for (i in list) {
                ed.add(
                    orderfoodModel(
                        i.itemName.toString(),
                        i.ids.toString(),
                        i.itemPrice.toString(),
                        "1",
                        i.count.toString()
                    )
                )
            }

            val idse = SessionMaintainence!!.instance!!.Uid!! + Timestamp.now().toString()
            val model = orederModel(
                idse,
                id.toString(),
                price.toString(),
                price.toString(),
                "cash",
                SessionMaintainence.instance!!.delicha!!,
                add,
                land,
                "Boss",
                Timestamp.now(),
                ed,
                SessionMaintainence.instance!!.lat.toString(),
                SessionMaintainence.instance!!.long.toString(),
                "open",
                "",
                "",
                SessionMaintainence.instance!!.fullname!!,
                SessionMaintainence!!.instance!!.addressverify!!,
                SessionMaintainence!!.instance!!.phoneno!!,
                SessionMaintainence!!.instance!!.Uid!!,
                "open",
                "",
                SessionMaintainence.instance!!.oru.toString()
            )

            try {
                val doc = firestoreDB!!.collection("order").document(idse)
                    .set(model)
                doc.addOnSuccessListener {

//                    imaged.visibility = View.GONE

                    try {
//                    image.visibility = View.VISIBLE
//                    image.setAnimation("foodorder.json")
//                    image.playAnimation()
//                    mWordViewModel!!.deleteall()
                        ViewModel!!.deleteAll()
                        showDialogsd()
//                    launch {
//                        delay(1800)
//                        withContext(Dispatchers.Main) {
//                            image.visibility = View.GONE
//                            finish()

//                            startActivity<MarketActivity>("from" to "order")
//                        }
//                    }
                    } catch (e: Exception) {
                        ViewModel!!.deleteAll()
                    }


                }
                doc.addOnFailureListener {
                    activity!!.toast("Something went wrong")

                    try {
//                        image.visibility = View.GONE
//                        imaged.visibility = View.GONE
                    } catch (e: Exception) {
                    }

                }

            } catch (e: Exception) {
            }

//            val ed = arrayListOf<GroceryItems>()
//            for (i in list) {
//                ed.add(
//                    GroceryItems(
//                        i.name.toString(),
//                        i.itemids.toString(),
//                        i[0].price.toString(),
//                        i[0].originalprize.toString(),
//                        i.size.toString()
//                    )
//                )
//            }
//
//            postOrder(
//                cart[0][0].itemids!!,
//                oprice,
//                price.toInt(),
//                "cash",
//                "30",
//                toString3,
//                toString2,
//                cart[0][0].name,
//                Timestamp.now(), ed
//
//            )
        }
    }


    private fun showDialog() {
        val fragmentManager = activity!!.supportFragmentManager
        val newFragment = dialog()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
    }

    private fun showDialogs() {
        val fragmentManager = activity!!.supportFragmentManager
        val newFragment = dialogew()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
    }

    private fun showDialogsd() {
        val fragmentManager = activity!!.supportFragmentManager
        val newFragment = dialogewd()
        val transaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit()
    }

}