package com.shurik.memwor_24.memwor

import android.content.Context
import com.shurik.memwor_24.memwor.settings.Settings
import kotlinx.coroutines.withContext

class Constants {

    companion object {
        const val ACCESS_TOKEN_VK =
            "9fb466189fb466189fb46618449ca5442599fb49fb46618fce51db6b049cb80918bb78e"
        const val ACCESS_TOKEN_REDDIT = "2527045170652-p6UZtNG6_5Vhuvvwwq55XFWjtVTQUQ"
        const val ACCESS_TOKEN_TELEGRAM = "6210441559:AAGMUMlMMs8NylhY7OXpK7yvKtIUY9VS6FU"
        const val ACCESS_TOKEN_TIKTOK = "ACCESS_TOKEN_TIKTOK"
        const val API_VERSION = "5.131"
        val CATEGORIES: Array<String> = arrayOf(
            "memes",
            "news",
            "games",
            "films",
            "meal",
            "books",
            "animals",
            "psychology",
            "sciences",
            "cartoons",
            "perfumery",
            "clothes",
            "household items",
            "chancellery",
            "gardening"
        )

        fun getCategories(context: Context): MutableMap<String, Boolean> {
            val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val categoriesList = mapOf<String, Boolean>().toMutableMap()

            categoriesList["memes"] = sharedPreferences.getBoolean("memes", true)
            categoriesList["news"] = sharedPreferences.getBoolean("news", true)
            categoriesList["games"] = sharedPreferences.getBoolean("games", true)
            categoriesList["films"] = sharedPreferences.getBoolean("films", true)
            categoriesList["meal"] = sharedPreferences.getBoolean("meal", true)
            categoriesList["books"] = sharedPreferences.getBoolean("books", true)
            categoriesList["animals"] = sharedPreferences.getBoolean("animals", true)
            categoriesList["gardening"] = sharedPreferences.getBoolean("gardening", true)
            categoriesList["psychology"] = sharedPreferences.getBoolean("psychology", true)
            categoriesList["sciences"] = sharedPreferences.getBoolean("sciences", true)
            categoriesList["cartoons"] = sharedPreferences.getBoolean("cartoons", true)
            categoriesList["perfumery"] = sharedPreferences.getBoolean("perfumery", true)
            categoriesList["clothes"] = sharedPreferences.getBoolean("clothes", true)
            categoriesList["householdItems"] = sharedPreferences.getBoolean("householdItems", true)
            categoriesList["chancellery"] = sharedPreferences.getBoolean("chancellery", true)
            return categoriesList
        }
    }
}

