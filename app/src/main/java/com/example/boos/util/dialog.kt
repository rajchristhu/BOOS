package com.example.boos.util

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.boos.R
import com.example.boos.start.Loginpage
import kotlinx.android.synthetic.main.dialog_frag.view.*
import org.jetbrains.anko.startActivity

class dialog : DialogFragment() {
    private var root_view: View? = null
    private lateinit var acceptHorizontalLayout: LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root_view = inflater.inflate(R.layout.dialog_frag, container, false)
//        acceptHorizontalLayout =
//            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        root_view!!.image.setAnimation("regi.json")

        root_view!!.image.playAnimation()
        root_view!!.image.loop(true)
        root_view!!.loginbutton.setOnClickListener {
            activity!!.startActivity<Loginpage>()
        }
        root_view!!.closd.setOnClickListener {
            dismiss()
        }
//        root_view!!.payrec!!.layoutManager = acceptHorizontalLayout
//        root_view!!.payrec!!.adapter = paymentadapter(activity)
        return root_view

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

}