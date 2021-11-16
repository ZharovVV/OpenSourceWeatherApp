package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.model.WidgetWeatherModel

class WidgetWeatherRepository(
    private val locationRepository: LocationRepository,
    private val weatherTodayRepository: WeatherTodayRepository
) {

    fun requestDataSync(): DataState<WidgetWeatherModel> {
        val locationModel = locationRepository.getLastKnownLocation()
            ?: return DataState.Error.buildWidgetError(
                errorTitle = "Для определения местоположения зайдите в приложение."
            )
        val weatherModelDataState: DataState<WeatherTodayModel> =
            weatherTodayRepository.requestDataSync(
                lat = locationModel.latitude,
                lon = locationModel.longitude
            )
        return when (weatherModelDataState) {
            is DataState.Success -> {
                val weatherModel = weatherModelDataState.data
                DataState.Success(
                    data = WidgetWeatherModel(
                        weatherIconId = weatherModel.iconId,
                        temperature = weatherModel.temperature,
                        locationDescription = locationModel.cityName,
                        forecastDateString = weatherModel.shortDateString
                    )
                )
            }
            else -> {
                DataState.Error.buildWidgetError(errorTitle = weatherModelDataState.message ?: "")
            }
        }
    }
}