package com.example.boos.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.boos.R
import com.google.firebase.firestore.FirebaseFirestore

class DealFormActivity : AppCompatActivity() {
    var firestoreDB: FirebaseFirestore? = null
    var key = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal_form)
        key = intent.getStringExtra("name").toString()
        firestoreDB = FirebaseFirestore.getInstance()

    }
}