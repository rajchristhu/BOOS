package com.example.boos.Room

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boos.Room.GroceryRepository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroceryViewModel(private val repository: GroceryRepository) : ViewModel() {
    private val mIssuePosts = ArrayList<GroceryItems>()
     val mIssuePostLiveData = MutableLiveData<List<GroceryItems>>()
    val names: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val namess: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    fun insertnamess(someValue: String) {
        namess.value = someValue

    }
    // In coroutines thread insert item in insert function.
    fun insert(item: GroceryItems) = GlobalScope.launch {
        repository.insert(item)
    }
    fun update(item: Int, i: String) = GlobalScope.launch {
        repository.update(item,i)
    }
    fun insertstr(someValue: String) {
        names.value = someValue
    }
    fun insertname(someValue: String) {
        name.value = someValue

    }

    // In coroutines thread delete item in delete function.
    fun delete(item: GroceryItems) = GlobalScope.launch {
        repository.delete(item)
    }
    fun deleteAll() = GlobalScope.launch {
        repository.deletes()
    }
    fun deletepar(i: String) = GlobalScope.launch {
        repository.deletepar(i)
    }

    fun addIssuePost(issuePost: List<GroceryItems>) {
        mIssuePosts.addAll(issuePost)
        mIssuePostLiveData.value = mIssuePosts
    }
    //Here we initialized allGroceryItems function with repository
    fun allGroceryItems() = repository.allGroceryItems()

}

