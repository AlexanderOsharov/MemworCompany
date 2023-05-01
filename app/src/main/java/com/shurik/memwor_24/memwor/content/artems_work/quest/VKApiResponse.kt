package com.shurik.memwor_24.memwor.content.artems_work.quest

import com.google.gson.annotations.SerializedName

data class GetVKJsonResponse(
    @SerializedName("response")
    val vkResponse: Response
)

data class Response(
    val count: Int,
    val items: List<Item>
)

data class Item(
    val marked_as_ads: Int,
    var text: String,
    val attachments: List<Attachment>,
)

data class Attachment(
    val photo: Photo,
    val type: String,
)

data class Photo(
    val access_key: String,
    val album_id: Int,
    val date: Int,
    val has_tags: Boolean,
    val id: Int,
    val owner_id: Int,
    val post_id: Int,
    val sizes: List<Size>,
    val text: String,
)

data class Size(
    val height: Int,
    val type: String,
    val url: String,
    val width: Int
)