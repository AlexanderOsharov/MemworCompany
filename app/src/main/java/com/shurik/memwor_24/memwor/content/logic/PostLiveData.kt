package com.shurik.memwor_24.memwor.content.logic

import androidx.lifecycle.LiveData
import com.shurik.memwor_24.memwor.content.Post

class PostLiveData: LiveData<MutableList<Post>>() {

    fun setValueToPosts(list:MutableList<Post>){
        postValue(list)
    }

    fun addPost(post: Post) {
        val updatedList = mutableListOf<Post>()
        updatedList.addAll(value ?: emptyList())
        updatedList.add(post)
        postValue(updatedList)
    }

    fun addPosts(newPosts: List<Post>) {
        val updatedList = mutableListOf<Post>()
        updatedList.addAll(value ?: emptyList())
        updatedList.addAll(newPosts)
        postValue(updatedList)
    }

    fun isEmpty(): Boolean {
        if (value.isNullOrEmpty()) return true
        return false
    }
}