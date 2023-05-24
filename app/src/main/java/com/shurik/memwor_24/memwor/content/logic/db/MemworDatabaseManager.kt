package com.shurik.memwor_24.memwor.content.logic.db

import android.util.Log
import android.widget.Toast
import com.shurik.memwor_24.memwor.content.logic.utils.Constants
import com.google.firebase.database.*
import com.shurik.memwor_24.memwor.content.logic.Domain
import com.shurik.memwor_24.memwor.content.logic.MemworViewModel
import com.shurik.memwor_24.memwor.content.new_platform.NewPlatformActivity
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


    fun addNewCommunity(platform: String, domain: String, name: String, category: String) {
        isCommunityExists(platform, domain) { exists ->
            if (!exists) {
                val id = db.key.toString()
                val newCommunity = Community(id, platform, domain, name, category)
                db.push().setValue(newCommunity)
            } else {
                println("Community with the same platform and domain already exists.")
            }
        }
    }

    private fun isCommunityExists(platform: String, domain: String, onComplete: (Boolean) -> Unit) {
        db.orderByChild("platform").equalTo(platform).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var exists = false
                for (communitySnapshot in snapshot.children) {
                    val community = communitySnapshot.getValue(Community::class.java)
                    if (community?.domain == domain) {
                        exists = true
                        break
                    }
                }
                onComplete(exists)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error: ${error.message}")
                onComplete(false)
            }
        })
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

    fun getTelegramDomains() {
        val telegramCoroutineScope = CoroutineScope(Dispatchers.IO)
        telegramCoroutineScope.launch {
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var _telegramData: MutableList<Domain> = ArrayList()
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
                            if (domain.platform == "telegram") {
                                Log.e("DATA", domain.domain)
                                Log.e("DATABASE SUCCESS","Success")
                                _telegramData.add(domain)
                            }
                        }

                    }
                    //Log.e("WITH LOVE FROM DB", _vkData.toString())
                    MemworViewModel.telegramDomainsLiveData.setValueToVkDomains(_telegramData)
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
