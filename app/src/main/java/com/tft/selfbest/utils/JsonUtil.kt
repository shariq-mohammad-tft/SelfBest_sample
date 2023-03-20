package com.tft.selfbest.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonUtil {
    companion object {

        @JvmStatic
        val apnaGson = Gson()

        inline fun <reified T> String?.fromJson(): T? {
            val gson = apnaGson
            val type = object : TypeToken<T?>() {}.type
            return gson.fromJson(this, type)
        }

        inline fun Any?.toJson(): String? {
            val gson = apnaGson
            return gson.toJson(this)
        }
    }
}