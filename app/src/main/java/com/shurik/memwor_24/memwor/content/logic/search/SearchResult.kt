package com.shurik.memwor_24.memwor.content.logic.search

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("snippet")
    val snippet: String,
    @SerializedName("pagemap")
    val pagemap: PageMap
)

data class PageMap(
    @SerializedName("cse_image")
    val cse_image: List<Map<String, String>>,
    @SerializedName("videoobject")
    val videoobject: List<Map<String, String>>
)