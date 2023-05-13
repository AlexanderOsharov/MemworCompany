package com.shurik.memwor_24.memwor.content.artems_work.quest

data class GetRedditJsonResponse(val data: Data)
data class Data(val children: List<Child>)
data class Child(val data: ChildData)
data class ChildData(
    val author: String,
    val subreddit: String,
    val selftext: String,
    val preview: Preview?
)

data class Preview(val images: List<Image>)
data class Image(val source: Source)
data class Source(val url: String)
