package com.shurik.memwor_24.memwor.content.logic.quest

import io.reactivex.Single
import retrofit2.http.*


interface QuestApi {
    @GET("./wall.get")
    fun getVkJson(@Query("domain") domain: String, @Query("access_token") access_token:String, @Query("count") count:Int, @Query("v") ver: String) : Single<GetVKJsonResponse>
    //@Field("domain") domain: String, @Field("access_token") access_token:String, @Field("v") ver: String
//    @GET("domain/hot.json")
//    fun getRedditJson(@Header("Authorization") reddit_token: String, @Query("domain") domain: String, ): Single<GetRedditJsonResponse>
    @GET("{domain}/hot.json")
    fun getRedditJson(@Header("Authorization") reddit_token: String, @Path("domain") domain: String, ) : Single<GetRedditJsonResponse>
}