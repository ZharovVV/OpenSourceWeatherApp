package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class Coordinates(
    val lon: Float,
    val lat: Float
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class MainInfo(
    val temp: Float,
    @SerializedName(value = "feels_like")
    val feelsLike: Float,
    @SerializedName(value = "temp_min")
    val tempMin: Float,
    @SerializedName(value = "temp_max")
    val tempMax: Float,
    val pressure: Int,
    @SerializedName(value = "sea_level")
    val seaLevel: Int,
    @SerializedName(value = "grnd_level")
    val grndLevel: Int,
    val humidity: Int
)

data class Wind(
    val speed: Float,
    val deg: Int,
    val gust: Float
)

data class Clouds(val all: Int)

data class SysInfo(
    val type: Int?,
    val id: Long?,
    val message: Float?,
    val country: String?,
    val sunrise: Long?,
    val sunset: Long?,
    val pod: String?
)