package com.example.boos.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// This is a data class which store data.
// Entities class create a table in database,
// in our database we will create three column

@Entity(tableName = "grocery_items")

data class GroceryItems(

    // create itemName variable to
    // store grocery items.
    @ColumnInfo(name = "itemName")
    var itemName: String,

    // create itemQuantity variable
    // to store grocery quantity.
    @ColumnInfo(name = "itemQuantity")
    var itemQuantity: String,
    @ColumnInfo(name = "ids")
    var ids: String,

    @ColumnInfo(name = "mainids")
    var mainids: String,
    @ColumnInfo(name = "count")
    var count: Int,
    // create itemPrice variable to
    // store grocery price.
    @ColumnInfo(name = "itemPrice")
    var itemPrice: Int
) {
    // Primary key is a unique key
    // for different database.
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}

