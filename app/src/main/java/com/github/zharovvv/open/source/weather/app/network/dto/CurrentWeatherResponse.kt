package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName(value = "coord")
    val coordinates: Coordinates,
    @SerializedName(value = "weather")
    val weather: List<Weather>,
    @SerializedName(value = "base")
    val base: String,
    @SerializedName(value = "main")
    val mainInfo: MainInfo,
    @SerializedName(value = "visibility")
    val visibility: Int,
    @SerializedName(value = "wind")
    val wind: Wind,
    @SerializedName(value = "clouds")
    val clouds: Clouds,
    @SerializedName(value = "dt")
    val dt: Long,
    @SerializedName(value = "sys")
    val sysInfo: SysInfo,
    @SerializedName(value = "timezone")
    val timezone: Long,
    @SerializedName(value = "id")
    val id: Long,
    @SerializedName(value = "name")
    val name: String,
    @SerializedName(value = "cod")
    val cod: Int
)