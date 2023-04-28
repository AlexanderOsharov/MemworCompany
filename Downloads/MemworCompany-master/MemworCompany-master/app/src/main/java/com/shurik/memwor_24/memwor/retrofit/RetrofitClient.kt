package com.shurik.memwor_24.memwor.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.reddit.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val redditInstance: RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }
}
