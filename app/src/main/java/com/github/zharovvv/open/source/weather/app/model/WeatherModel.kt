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

/**
 * Модель виджета погоды
 */
data class WidgetWeatherModel(
    @DrawableRes
    val weatherIconId: Int,
    val temperature: String,
    val locationDescription: String,
    val forecastDateString: String
)