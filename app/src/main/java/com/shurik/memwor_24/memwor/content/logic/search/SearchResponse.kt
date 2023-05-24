package com.shurik.memwor_24.memwor.content.logic.search

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items")
    val items: List<Result>
)
