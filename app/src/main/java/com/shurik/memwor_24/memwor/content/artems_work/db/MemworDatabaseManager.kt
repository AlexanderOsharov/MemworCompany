package com.shurik.memwor_24.memwor.content.artems_work.db

import android.util.Log
import com.shurik.memwor_24.memwor.content.artems_work.utils.Constants
import com.google.firebase.database.*
import com.shurik.memwor_24.memwor.content.artems_work.Domain
import com.shurik.memwor_24.memwor.content.artems_work.MemworViewModel
import kotlinx.coroutines.*

class MemworDatabaseManager {

    private var db: DatabaseReference
    private var const: Constants = Constants()
    private val COMMUNITY_KEY: String = "Community"

    init{
        db = FirebaseDatabase.getInstance().getReference(COMMUNITY_KEY)
    }

//    fun getVklist() = runBlocking{
//        getVkDomains()
//    }


    fun addNewCommunity(platform: String, domain: String, name: String, category: String){
        val id = db.key.toString()
        val newCommunity = Community(id, platform, domain, name, category)
        db.push().setValue(newCommunity)
    }

    fun getVkDomains() {
        val vkCoroutineScope = CoroutineScope(Dispatchers.IO)
        vkCoroutineScope.launch {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var _vkData: MutableList<Domain> = ArrayList()
                    //if (vkData.size > 0) vkData.clear()
                    for (ds: DataSnapshot in dataSnapshot.getChildren()) {
                        val community: Community? = ds.getValue(Community::class.java)
                        val domain = Domain()
                        if (community != null) {
                            community.category?.let { domain.category = it }
                            community.domain?.let { domain.domain = it }
                            community.name?.let { domain.name = it }
                            community.platform?.let { domain.platform = it }
                        }
                        if (!domain.name.isNullOrEmpty() || !domain.domain.isNullOrEmpty() || !domain.category.isNullOrEmpty() || !domain.platform.isNullOrEmpty()) {
                            if (domain.platform == "vk") {
                                _vkData.add(domain)
                            }
                        }
                    }
                    //Log.e("WITH LOVE FROM DB", _vkData.toString())
                    MemworViewModel.vkDomainsLiveData.setValueToVkDomains(_vkData)
                }

                //Log.e("SUCCESS DATABASE TAG", _vkData.toString())
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("DATABASE ERROR TAG", "Failed to read value.", error.toException())
                }
            })
        }

    }
    fun getRedditDomains() {
        val redditCoroutineScope = CoroutineScope(Dispatchers.IO)
        redditCoroutineScope.launch {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var _redditData: MutableList<Domain> = ArrayList()
                    //if (vkData.size > 0) vkData.clear()
                    for (ds: DataSnapshot in dataSnapshot.getChildren()) {
                        val community: Community? = ds.getValue(Community::class.java)
                        val domain = Domain()
                        if (community != null) {
                            community.category?.let { domain.category = it }
                            community.domain?.let { domain.domain = it }
                            community.name?.let { domain.name = it }
                            community.platform?.let { domain.platform = it }
                        }
                        if (!domain.name.isNullOrEmpty() || !domain.domain.isNullOrEmpty() || !domain.category.isNullOrEmpty() || !domain.platform.isNullOrEmpty()) {
                            if (domain.platform == "reddit") {
                                Log.e("DATA", domain.domain)
                                Log.e("DATABASE SUCCESS","Success")
                                _redditData.add(domain)
                            }
                        }

                    }
                    //Log.e("WITH LOVE FROM DB", _vkData.toString())
                    MemworViewModel.redditDomainsLiveData.setValueToVkDomains(_redditData)
                }

                //Log.e("SUCCESS DATABASE TAG", _vkData.toString())
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("DATABASE ERROR TAG", "Failed to read value.", error.toException())
                }
            })
        }
    }
        //result.join()
        //Log.e("SUCCEED res DB", res.toString())
        //Log.e("SUCCEED vkData DB", vkData.toString())




}
