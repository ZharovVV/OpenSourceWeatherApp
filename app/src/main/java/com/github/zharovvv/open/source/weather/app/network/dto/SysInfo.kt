package com.github.zharovvv.open.source.weather.app.network.dto

data class SysInfo(
    val type: Int,
    val id: Long,
    val message: Float,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)