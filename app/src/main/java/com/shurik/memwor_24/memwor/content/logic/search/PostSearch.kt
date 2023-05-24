package com.shurik.memwor_24.memwor.content.logic.search

import android.util.Log
import com.shurik.memwor_24.memwor.Constants
import com.shurik.memwor_24.memwor.content.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import javax.xml.parsers.DocumentBuilderFactory

class PostSearch(private val apiKey: String) {
    @Throws(IOException::class)
    fun search(query: String?): Post? {
        val post = Post()
        val encodedQuery: String = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        val urlString = "https://yandex.ru/search/?text=$encodedQuery&lr=213"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val input = BufferedReader(InputStreamReader(connection.inputStream))
        val content = input.readText()
        input.close()
        connection.disconnect()

        // Clean the content
        val cleanedContent = cleanContent(content)

        val documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val inputStream = cleanedContent.byteInputStream()
        val document = documentBuilder.parse(inputStream)
        val items: NodeList = document.getElementsByTagName("li")
        for (i in 0 until items.length) {
            val item = items.item(i) as Element
            val classNames = item.getAttribute("class")
            if (classNames.contains("serp-item") && classNames.contains("serp-item_card")) {
                post.text = item.textContent
                val images = item.getElementsByTagName("img")
                for (j in 0 until images.length) {
                    val image = images.item(j) as Element
                    post.images.add(image.getAttribute("src"))
                }
                val videos = item.getElementsByTagName("a")
                for (j in 0 until videos.length) {
                    val video = videos.item(j) as Element
                    val classAttr = video.getAttribute("class")
                    if (classAttr.contains("video-preview")) {
                        post.videos.add(video.getAttribute("href"))
                    }
                }
                break
            }
        }
        Log.e("YandexPost", post.toString())
        return post
    }

    private fun cleanContent(content: String): String {
        return content.replace("&", "&amp;")
    }
}



