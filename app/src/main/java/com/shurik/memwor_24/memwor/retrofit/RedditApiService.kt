package com.shurik.memwor_24.memwor.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RedditApiService {
    @GET("r/{domain}/hot")
    fun getRedditCommunityInfo(
        @Header("Authorization") token: String,
        @Query("domain") domain: String,
    ): Call<RedditApiResponse>
}
