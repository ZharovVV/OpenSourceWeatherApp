package com.github.zharovvv.open.source.weather.app.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.fromJsonTo(jsonString: String): T {
    return this.fromJson(jsonString, object : TypeToken<T>() {}.type)
}

inline fun <reified T> Gson.fromJson(jsonString: String): T {
    @Suppress("RemoveExplicitTypeArguments")
    return this.fromJsonTo<T>(jsonString)
}