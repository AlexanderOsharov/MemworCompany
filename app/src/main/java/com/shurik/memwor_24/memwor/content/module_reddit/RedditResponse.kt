package com.shurik.memwor_24.memwor.content.module_reddit

data class RedditResponse(val data: Data)
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
