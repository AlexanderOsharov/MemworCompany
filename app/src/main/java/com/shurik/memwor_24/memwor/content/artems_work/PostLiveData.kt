package com.shurik.memwor_24.memwor.content.artems_work

import androidx.lifecycle.LiveData
import com.shurik.memwor_24.memwor.content.Post

class PostLiveData: LiveData<MutableList<Post>>() {

    fun setValueToVkPosts(list:MutableList<Post>){
        postValue(list)
    }

    fun isEmpty(): Boolean {
        if (value.isNullOrEmpty()) return true
        return false
    }
}