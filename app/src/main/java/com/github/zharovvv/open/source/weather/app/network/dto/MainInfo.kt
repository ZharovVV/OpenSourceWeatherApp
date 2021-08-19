package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class MainInfo(
    val temp: Float,
    @SerializedName(value = "feels_like")
    val feelsLike: Float,
    @SerializedName(value = "temp_min")
    val tempMin: Float,
    @SerializedName(value = "temp_max")
    val tempMax: Float,
    val pressure: Int,
    val humidity: Int
)
