package com.github.zharovvv.open.source.weather.app.models.presentation

/**
 * Модель карточек почасовой погоды
 */
data class HourlyWeatherModel(
    val items: List<HourlyWeatherItemModel>
)