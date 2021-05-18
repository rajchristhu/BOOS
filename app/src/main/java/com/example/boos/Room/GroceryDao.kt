package com.example.boos.Room

import androidx.lifecycle.LiveData
import androidx.room.*

// This class is used to create
// function for database.
@Dao
interface GroceryDao {

    // Insert function is used to
    // insert data in database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: GroceryItems)

    // Delete function is used to
    // delete data in database.
    @Delete
    suspend fun delete(item: GroceryItems)

    @Query("DELETE FROM grocery_items WHERE ids=:item")
    suspend fun deletepar(item: String)

    @Query("UPDATE grocery_items SET count=:habit WHERE ids=:i")
    fun update(habit: Int, i: String): Int

    @Query("DELETE FROM grocery_items")
    fun deleteAll()
    // getAllGroceryItems function is used to get
    // all the data of database.
    @Query("SELECT * FROM grocery_items")
    fun getAllGroceryItems(): LiveData<List<GroceryItems>>
}

