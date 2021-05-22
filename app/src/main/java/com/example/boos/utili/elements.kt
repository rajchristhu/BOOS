package com.example.boos.utili

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService


class elements {
    var progressDialog: ProgressDialog? = null
    fun progressbarshow(context: Context) {
        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage("Downloading Video ...")
        progressDialog!!.show()
        // To Dismiss progress dialog
        //progressDialog.dismiss()
    }

    fun progressDialogdismiss(context: Context) {
        progressDialog = ProgressDialog(context)

        progressDialog!!.dismiss()

    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

}
