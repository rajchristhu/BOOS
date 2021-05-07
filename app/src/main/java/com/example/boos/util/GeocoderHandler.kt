package com.example.boos.util

import android.os.Handler
import android.os.Message
import android.util.Log

class GeocoderHandler : Handler() {
    override fun handleMessage(message: Message) {
        val locationAddress: String?
        when (message.what) {
            1 -> {
                val bundle = message.data
                locationAddress = bundle.getString("address")
            }
            else -> locationAddress = null
        }
//        Log.e("class", locationAddress)
    }
}
