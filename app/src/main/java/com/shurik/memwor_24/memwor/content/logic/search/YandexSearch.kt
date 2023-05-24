package com.shurik.memwor_24.memwor.content.logic.search

import android.util.Log
import com.shurik.memwor_24.memwor.content.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class YandexSearch {
    private val yandexSearchUrl = "https://search.yandex.com/search"
    private val httpClient = OkHttpClient()

    suspend fun search(query: String): List<Post> = withContext(Dispatchers.IO) {
        val url = yandexSearchUrl.toHttpUrlOrNull()?.newBuilder()
            ?.addQueryParameter("text", query)
            ?.build()

        val request = Request.Builder()
            .url(url!!)
            .build()

        val response = httpClient.suspendCall(request)
        val responseBody = response.body?.string() ?: ""

        val json = JSONObject(responseBody)
        val results = json.getJSONArray("results")
        val posts = mutableListOf<Post>()
        Log.e("Json: ", json.toString())
        for (i in 0 until results.length()) {
            val result = results.getJSONObject(i)
            val post = parsePost(result)
            posts.add(post)
        }

        Log.e("YandexSearch: ", posts.toString())

        return@withContext posts
    }

    private suspend fun OkHttpClient.suspendCall(request: Request): Response {
        return suspendCancellableCoroutine { continuation ->
            val call = newCall(request)
            continuation.invokeOnCancellation { call.cancel() }
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }
            })
        }
    }

    private fun parsePost(json: JSONObject): Post {
        val post = Post()
        post.images = parseUrls(json.getJSONArray("images"))
        post.text = json.getString("text")
        post.videos = parseUrls(json.getJSONArray("videos"))
        post.category = json.optString("category", "")
        post.author = json.getString("author")

        return post
    }

    private fun parseUrls(jsonArray: JSONArray): MutableList<String> {
        val urls = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            urls.add(jsonArray.getString(i))
        }

        return urls
    }
}
