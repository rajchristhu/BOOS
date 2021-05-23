package com.example.boos.model

import com.google.firebase.Timestamp

data class orederModel(
    val orderId: String = "",
    val shopId: String = "",
    val originalPrice: String = "",
    val totalPrice: String = "",
    val paymentMethod: String = "",
    val deliveryDuration: String = "",
    val userAddress: String = "",
    val landmark: String = "",
    val shopName: String = "",
    val postedTime: Timestamp? = null,
    val foodItems: ArrayList<orderfoodModel>? = null,
    val lat: String = "",
    val long: String = "",
    val status: String = "",
    val deleveryperson: String = "",
    val deleverypersonPhoneno: String = "",
    val userName: String = "",
    val address: String = "",
    val userpho: String = "",
    val userId: String = "",
    val paymentStatus: String = "",
    val deliId: String = "",
    val oru: String = ""



)