package com.shurik.memwor_24.memwor.content.artems_work

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