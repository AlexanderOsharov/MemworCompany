package com.shurik.memwor_24.memwor.content.new_platform

import android.util.Log
import android.widget.Toast
import com.shurik.memwor_24.memwor.Constants
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class CheckPlatform {
    companion object {
        // Проверка существования и открытости сообщества в Reddit
        fun checkRedditGroup(
            subredditName: String,
            redditDomain: String = "https://reddit.com",
            token: String = Constants.ACCESS_TOKEN_REDDIT
        ): Boolean {
            val url: URL

            // Проверка корректности URL
            try {
                url = URL("$redditDomain/r/$subredditName/about.json")
            } catch (e: MalformedURLException) {
                Log.e("ErrorReddit", "Некорректный URL: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
                return false
            }
            try {
                val connection = url.openConnection()
                connection.setRequestProperty("Authorization", "Bearer $token")
                val content = connection.getInputStream().bufferedReader().use { it.readText() }
                val json = JSONObject(content)
                val isPrivate = json.getJSONObject("data").getString("subreddit_type") == "private"
                return !isPrivate
            } catch (e: IOException) {
                Log.e("ErrorVk", "Ошибка при подключении: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            } catch (e: Exception) {
                Log.e("ErrorVk", "Неизвестная ошибка: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            }
            return false
        }

        // Проверка существования и открытости сообщества во ВКонтакте
        fun checkVkGroup(domain: String, token: String = Constants.ACCESS_TOKEN_VK): Boolean {
            val url: URL

            // Проверка корректности URL
            try {
                url = URL("https://api.vk.com/method/groups.getById?group_id=$domain&access_token=$token&v=5.130")
            } catch (e: MalformedURLException) {
                Log.e("ErrorTelegram", "Некорректный URL: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
                return false
            }
            try {
                val content = url.readText()
                val json = JSONObject(content)
                val isClosed =
                    json.getJSONArray("response").getJSONObject(0).getInt("is_closed") == 1
                return !isClosed
            } catch (e: IOException) {
                Log.e("ErrorVk", "Ошибка при подключении: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            } catch (e: Exception) {
                Log.e("ErrorVk", "Неизвестная ошибка: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            }
            return false
        }

        // Проверка существования и открытости сообщества в TikTok
        fun checkTikTokGroup(
            accountName: String,
            tiktokDomain: String = "https://tiktok.com"
        ): Boolean {
            val url: URL

            // Проверка корректности URL
            try {
                url = URL("$tiktokDomain/@$accountName")
            } catch (e: MalformedURLException) {
                Log.e("ErrorTikTok", "Некорректный URL: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
                return false
            }
            try {
                val doc = Jsoup.connect(url.toString()).get()
                val metaDescription = doc.select("meta[name=description]").attr("content")
                return !metaDescription.contains("This account is private")
            } catch (e: IOException) {
                Log.e("ErrorTikTok", "Ошибка при подключении: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            } catch (e: Exception) {
                Log.e("ErrorTikTok", "Неизвестная ошибка: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            }
            return false
        }

        // Проверка существования и открытости сообщества в Telegram
        fun checkTelegramGroup(name: String, domain: String = "t.me"): Boolean {
            val url: URL

            // Проверка корректности URL
            try {
                url = URL("https://$domain/$name")
            } catch (e: MalformedURLException) {
                Log.e("ErrorTelegram", "Некорректный URL: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
                return false
            }

            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36"
                )
                connection.connect()
                val responseCode = connection.responseCode

                if (responseCode == 200) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val pageContent = reader.readText()
                    reader.close()

                    val openGroupIndicator = "<div class=\"tgme_page_extra\">"
                    if (pageContent.contains(openGroupIndicator)) {
                        connection.disconnect()
                        return true
                    }
                }
                connection.disconnect()
            } catch (e: IOException) {
                Log.e("ErrorTelegram", "Ошибка при подключении: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            } catch (e: Exception) {
                Log.e("ErrorTelegram", "Неизвестная ошибка: $e")
                // Действия, при возникновении ошибки, например, показать сообщение пользователю.
            }
            return false
        }
    }
}
