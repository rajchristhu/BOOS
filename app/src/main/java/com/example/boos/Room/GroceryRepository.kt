package com.example.boos.Room

import com.example.boos.Room.GroceryDatabase


class GroceryRepository(private val db: GroceryDatabase) {

    suspend fun insert(item: GroceryItems) = db.getGroceryDao().insert(item)
    suspend fun update(item: Int, i: String) = db.getGroceryDao().update(item,i)
    suspend fun delete(item: GroceryItems) = db.getGroceryDao().delete(item)
    suspend fun deletes() = db.getGroceryDao().deleteAll()
    suspend fun deletepar(i: String) = db.getGroceryDao().deletepar(i)

    fun allGroceryItems() = db.getGroceryDao().getAllGroceryItems()
}

