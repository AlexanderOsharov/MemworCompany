package com.shurik.memwor_24.memwor.content.logic

import androidx.lifecycle.LiveData

class DomainsLiveData: LiveData<MutableList<Domain>>() {

    fun setValueToVkDomains(list:MutableList<Domain>){
        value = list
    }

    fun isEmpty(): Boolean {
        if (value.isNullOrEmpty()) return true
        return false
    }
}