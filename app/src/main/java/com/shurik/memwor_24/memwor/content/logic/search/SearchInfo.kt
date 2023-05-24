package com.shurik.memwor_24.memwor.content.logic.search

import android.util.Log
import com.google.gson.Gson
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.Post
import com.squareup.moshi.Json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class SearchInfo {
    companion object{
        suspend fun GoogleSearch(query: String): List<Post> = withContext(Dispatchers.IO) {
            val apiKey = Constants.ACCESS_TOKEN_GOOGLE
            val searchEngineId = Constants.GOOGLE_SEARCH_ENGINE_ID
            val url = "https://www.googleapis.com/customsearch/v1?key=$apiKey&cx=$searchEngineId&q=$query"

            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()

            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw RuntimeException("Failed to connect: ${connection.responseCode}")
            }

            val gson = Gson()
            val json = InputStreamReader(connection.inputStream).use { it.readText() }
            val searchResponse = gson.fromJson(json, SearchResponse::class.java)

            connection.disconnect()
            Log.e("GoogleSearch", json)
            return@withContext searchResponse.items.map { result ->
                Post().apply {
                    text = result.snippet
                    images = result.pagemap?.cse_image?.mapNotNull { it["src"] }
                        ?.toMutableList() ?: mutableListOf()
                    videos = result.pagemap?.videoobject?.mapNotNull { it["contenturl"] }
                        ?.filter { isVideoAccessible(it) }
                        ?.toMutableList() ?: mutableListOf()
                    // Здесь можно добавить обработку для category и author, если они доступны.
                }
            }
        }
        suspend fun isVideoAccessible(url: String): Boolean = withContext(Dispatchers.IO) {
            return@withContext try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.connect()
                val isAccessible = connection.responseCode == HttpURLConnection.HTTP_OK
                connection.disconnect()
                isAccessible
            } catch (e: Exception) {
                false
            }
        }

        suspend fun HabrSearch(query: String): List<Post> = withContext(Dispatchers.IO) {
            val url = "https://habr.com/ru/search/?target_type=posts&q=$query"
            val document: Document = Jsoup.connect(url).get()
            val postElements = document.select(".post.post_preview")
            Log.i("HabrJson", document.toString())
            val posts = mutableListOf<Post>()

            postElements.forEach { postElement ->
                val post = Post()

                post.text = postElement.select(".post__title_link").text()
                post.category = postElement.select(".hub-link").text()
                post.author = postElement.select(".user-info__nickname").text()

                postElement.select(".post__body img[src]").forEach { img ->
                    val imageUrl = img.attr("src")
                    post.images.add(imageUrl)
                }

                // Видео контент не найден на habr.com, так что этот список будет пустым.

                // Проверяем, есть ли изображение и внушительный размер текста/статьи
                val postTextLength = postElement.select(".post__text").text().length
                val minTextLength = 300 // Устанавливаем минимальную длину текста/статьи

                if (post.images.isNotEmpty() && postTextLength >= minTextLength) {
                posts.add(post)
                Log.i("HabrSearch", post.toString())
            }
            }
            Log.i("Habr: ", posts.toString())
            return@withContext posts
        }


        suspend fun VkSearch(query: String, accessToken: String = Constants.ACCESS_TOKEN_VK): List<Post> = withContext(Dispatchers.IO) {
            val client = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

            val posts = mutableListOf<Post>()

            val url =
                "https://api.vk.com/method/newsfeed.search?q=$query&access_token=$accessToken&v=5.131&count=50"

            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val json = JSONObject(response.body!!.string())
                    val items = json.getJSONObject("response").getJSONArray("items")

                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)

                        val post = Post()
                        post.text = item.getString("text")

                        if (item.has("attachments")) {
                            val attachments = item.getJSONArray("attachments")
                            for (j in 0 until attachments.length()) {
                                val attachment = attachments.getJSONObject(j)
                                when (attachment.getString("type")) {
                                    "photo" -> {
                                        val sizes = attachment.getJSONObject("photo").getJSONArray("sizes")
                                        val imageUrl = sizes.getJSONObject(sizes.length() - 1).getString("url")
                                        post.images.add(imageUrl)
                                    }
                                    "video" -> {
                                        val videoOwnerId = attachment.getJSONObject("video").getString("owner_id")
                                        val videoId = attachment.getJSONObject("video").getString("id")
                                        val videoUrl = "https://vk.com/video$videoOwnerId" + "_" + "$videoId"
                                        post.videos.add(videoUrl)
                                    }
                                }
                            }
                        }

                        post.category = ""
                        post.author = item.getString("owner_id")

                        posts.add(post)
                        Log.e("VkSearch", post.toString())
                        Log.e("VkSearch", post.author)
                        if (post.images.isNotEmpty()) {
                            Log.e("VkSearch", post.images[0])
                        }
                        if (post.videos.isNotEmpty()) {
                            Log.e("VkSearch", post.videos[0])
                        }
                    }
                } else {
                    throw Exception("Не удалось выполнить запрос: ${response.message}")
                }
            }

            Log.e("VkPost: ", posts.toString())

            return@withContext posts
        }

        suspend fun WikipediaSearch(query: String): List<Post> = withContext(Dispatchers.IO) {
            val url = "https://en.wikipedia.org/w/api.php?action=query&list=search&format=json&srsearch=$query&srlimit=10&prop=info&inprop=url&utf8="
            val response = URL(url).readText()
            val data = JSONObject(response)
            val searchResults = data.getJSONObject("query").getJSONArray("search")
            val posts = mutableListOf<Post>()

            for (i in 0 until searchResults.length()) {
                val result = searchResults.getJSONObject(i)
                val title = result.getString("title")
                val pageId = result.getInt("pageid")
                val contentUrl = "https://en.wikipedia.org/?curid=$pageId"

                // Получаем содержимое страницы
                val document: Document = Jsoup.connect(contentUrl).get()
                val content = document.select("#mw-content > div.mw-parser-output > p")
                val contentText = content.joinToString(separator = "\n") { it.text() }

                // Получаем изображение (если есть)
                val imageUrl = document.select("#-content-text > div.mw-parser-output img[src]").firstOrNull()?.attr("src")

                // Проверяем размер статьи и наличие изображения
                val minTextLength = 300 // Устанавливаем минимальную длину текста/статьи

                if (contentText.length >= minTextLength && imageUrl != null) {
                    val post = Post()
                    post.author = title
                    post.text = contentText
                    post.images.add(imageUrl)
                    posts.add(post)
                    Log.e("WikipediaSearch", post.toString())
                }
            }

            return@withContext posts
        }

    }
}