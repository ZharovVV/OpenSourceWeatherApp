package com.github.zharovvv.open.source.weather.app.model

import androidx.annotation.DrawableRes

data class WeatherTodayModel(
    @DrawableRes
    val iconId: Int,
    val description: String,
    val dateString: String,
    val temperature: String,
    val detailedWeatherParams: List<DetailedWeatherParamModel>
)
