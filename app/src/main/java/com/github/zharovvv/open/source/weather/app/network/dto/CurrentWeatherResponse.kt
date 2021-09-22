package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class Coordinates(val lon: Float, val lat: Float)

data class Weather(val id: Int, val main: String, val description: String, val icon: String)

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

data class Wind(val speed: Float, val deg: Int)

data class Clouds(val all: Int)

data class SysInfo(
    val type: Int,
    val id: Long,
    val message: Float,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class CurrentWeatherResponse(
    @SerializedName(value = "coord")
    val coordinates: Coordinates,
    val weather: List<Weather>,
    val base: String,
    @SerializedName(value = "main")
    val mainInfo: MainInfo,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    @SerializedName(value = "sys")
    val sysInfo: SysInfo,
    val timezone: Long,
    val id: Long,
    val name: String,
    val cod: Int
)