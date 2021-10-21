package com.github.zharovvv.open.source.weather.app.model

import androidx.annotation.DrawableRes
import com.github.zharovvv.open.source.weather.app.util.TimeIndicator

/**
 * Модель карточки текущей погоды
 */
data class WeatherTodayModel(
    @DrawableRes
    val iconId: Int,
    val description: String,
    val dateString: String,
    val temperature: String,
    val detailedWeatherParams: List<DetailedWeatherParamModel>
)

data class DetailedWeatherParamModel(
    val name: String,
    @DrawableRes
    val iconId: Int,
    val value: String
)

/**
 * Модель карточек почасовой погоды
 */
data class HourlyWeatherModel(
    val items: List<HourlyWeatherItemModel>
)

data class HourlyWeatherItemModel(
    val now: Boolean,
    val timeIndicator: TimeIndicator,
    val timeString: String,
    @DrawableRes
    val iconId: Int,
    val value: String
)

/**
 * Модель списка ежедневного прогназа погоды
 */
data class WeekWeatherModel(
    val items: List<WeekWeatherItemModel>
)

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