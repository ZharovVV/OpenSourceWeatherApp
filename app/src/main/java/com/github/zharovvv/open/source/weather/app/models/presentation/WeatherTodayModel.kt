package com.github.zharovvv.open.source.weather.app.models.presentation

import androidx.annotation.DrawableRes

/**
 * Модель карточки текущей погоды
 */
data class WeatherTodayModel(
    @DrawableRes
    val iconId: Int,
    val description: String,
    /**
     * Дата прогноза в формате "EEEE, dd MMM"
     */
    val dateString: String,
    /**
     * Дата прогноза в формате "dd.MM, HH:mm"
     */
    val shortDateString: String,
    val temperature: String,
    val detailedWeatherParams: List<DetailedWeatherParamModel>,
    /**
     * Данные о местоположении, для которого была определена текущая погода.
     */
    val locationModel: LocationModel
)