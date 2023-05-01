package com.shurik.memwor_24.memwor.content.artems_work.quest

import io.reactivex.Single
import retrofit2.http.*


interface QuestApi {
    @GET("./wall.get")
    fun getVkJson(@Query("domain") domain: String, @Query("access_token") access_token:String, @Query("count") count:Int, @Query("v") ver: String) : Single<GetVKJsonResponse>
    //@Field("domain") domain: String, @Field("access_token") access_token:String, @Field("v") ver: String
    @GET("./oddlysatisfying.json")
    fun getRedditJson() : Single<GetVKJsonResponse>
    //TODO(Telegram parsing) (add data classes to telegram fun)
    //fun getTelegramJson() : Single<String>
}