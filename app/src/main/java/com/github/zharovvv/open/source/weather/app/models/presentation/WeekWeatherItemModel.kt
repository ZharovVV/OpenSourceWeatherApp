package com.github.zharovvv.open.source.weather.app.models.presentation

import androidx.annotation.DrawableRes

data class WeekWeatherItemModel(
    @DrawableRes
    val iconId: Int,
    /**
     * День недели прогноза
     */
    val dayOfWeekOfForecast: String,
    /**
     * Дата прогноза
     */
    val forecastDate: String,
    val maxTemperature: String,
    val minTemperature: String
)