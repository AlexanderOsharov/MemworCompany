package com.shurik.memwor_24.memwor.content.new_platform

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import org.eclipse.jetty.client.HttpClient

object CategoryDetector {

    private val keywordsByCategory = mapOf(
        "memes" to listOf("мемы", "мем", "смешно", "юмор", "приколы"),
        "news" to listOf("новости", "актуальное", "события", "политика", "обзор"),
        "games" to listOf("игры", "гейминг", "прохождение", "обзор", "консоли"),
        "films" to listOf("кино", "фильмы", "сериалы", "актеры", "режиссеры"),
        "meal" to listOf("еда", "рецепты", "кулинария", "блюда", "кухня"),
        "books" to listOf("книги", "литература", "авторы", "читать", "романы"),
        "animals" to listOf("животные", "домашние питомцы", "кошки", "собаки", "зоопарк"),
        "psychology" to listOf("психология", "саморазвитие", "мотивация", "личность", "отношения"),
        "sciences" to listOf("наука", "технологии", "исследования", "обучение", "изобретения"),
        "cartoons" to listOf("мультфильмы", "анимация", "дисней", "персонажи", "студии"),
        "perfumery" to listOf("парфюмерия", "ароматы", "духи", "товары", "косметика"),
        "clothes" to listOf("одежда", "мода", "стиль", "бренды", "ткани"),
        "household items" to listOf("бытовые товары", "техника", "мебель", "интерьер", "аксессуары"),
        "chancellery" to listOf("канцелярские товары", "принадлежности", "органайзеры", "бумага", "ручки"),
        "gardening" to listOf("садоводство", "растения", "цветы", "огород", "инструменты")
    )


    fun detectCategories(content: String, userSelectedCategories: List<String>): List<String> {
        val detectedCategories = mutableMapOf<String, Int>()

        for ((category, keywords) in keywordsByCategory) {
            val count = keywords.count { keyword -> content.contains(keyword, ignoreCase = true) }
            if (count > 0) {
                detectedCategories[category] = count
            }
        }

        return if (detectedCategories.isNotEmpty()) {
            detectedCategories.entries.sortedByDescending { it.value }.take(3).map { it.key }
        } else {
            userSelectedCategories
        }
    }

    suspend fun detectCategoriesWithSynonyms(content: String, userSelectedCategories: List<String>): List<String> {
        val detectedCategories = mutableMapOf<String, Int>()

        for ((category, keywords) in keywordsByCategory) {
            val contentWords = content.split(" ").map { it.trim() }
            val count = contentWords.count { word ->
                keywords.any { keyword ->
                    word.equals(keyword, ignoreCase = true) || isSynonym(word, keyword)
                }
            }

            if (count > 0) {
                detectedCategories[category] = count
            }
        }

        return if (detectedCategories.isNotEmpty()) {
            detectedCategories.entries.sortedByDescending { it.value }.take(3).map { it.key }
        } else {
            userSelectedCategories
        }
    }

    @Serializable
    data class DatamuseWord(val word: String)

    suspend fun isSynonym(word1: String, word2: String): Boolean {
        val client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

        val url = "https://api.datamuse.com/words?ml=$word1"
        val synonyms: List<DatamuseWord> = client.get(url)

        client.close()

        return synonyms.any { it.word.equals(word2, ignoreCase = true) }
    }
}
