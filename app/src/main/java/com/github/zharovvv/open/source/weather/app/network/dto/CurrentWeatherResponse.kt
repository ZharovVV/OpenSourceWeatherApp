package com.github.zharovvv.open.source.weather.app.network.dto

import com.google.gson.annotations.SerializedName

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