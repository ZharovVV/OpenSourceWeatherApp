package com.github.zharovvv.open.source.weather.app.models.data.remote

import com.google.gson.annotations.SerializedName

data class Coordinates(
    @SerializedName(value = "lon")
    val lon: Float,
    @SerializedName(value = "lat")
    val lat: Float
)

data class Weather(
    @SerializedName(value = "id")
    val id: Int,
    @SerializedName(value = "main")
    val main: String,
    @SerializedName(value = "description")
    val description: String,
    @SerializedName(value = "icon")
    val icon: String
)

data class MainInfo(
    @SerializedName(value = "temp")
    val temp: Float,
    @SerializedName(value = "feels_like")
    val feelsLike: Float,
    @SerializedName(value = "temp_min")
    val tempMin: Float,
    @SerializedName(value = "temp_max")
    val tempMax: Float,
    @SerializedName(value = "pressure")
    val pressure: Int,
    @SerializedName(value = "sea_level")
    val seaLevel: Int,
    @SerializedName(value = "grnd_level")
    val grndLevel: Int,
    @SerializedName(value = "humidity")
    val humidity: Int
)

data class Wind(
    @SerializedName(value = "speed")
    val speed: Float,
    @SerializedName(value = "deg")
    val deg: Int,
    @SerializedName(value = "gust")
    val gust: Float
)

data class Clouds(
    @SerializedName(value = "all")
    val all: Int
)

data class SysInfo(
    @SerializedName(value = "type")
    val type: Int?,
    @SerializedName(value = "id")
    val id: Long?,
    @SerializedName(value = "message")
    val message: Float?,
    @SerializedName(value = "country")
    val country: String?,
    @SerializedName(value = "sunrise")
    val sunrise: Long?,
    @SerializedName(value = "sunset")
    val sunset: Long?,
    @SerializedName(value = "pod")
    val pod: String?
)