package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.models.domain.DataState
import com.github.zharovvv.open.source.weather.app.models.presentation.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WidgetWeatherModel
import io.reactivex.Observable

//TODO Похоже что класс на самом деле является интерактором
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
            weatherTodayRepository.requestDataSync(locationModel)
        return when (weatherModelDataState) {
            is DataState.Success -> {
                val weatherModel = weatherModelDataState.data
                DataState.Success(
                    data = WidgetWeatherModel(
                        weatherIconId = weatherModel.iconId,
                        temperature = weatherModel.temperature,
                        locationDescription = weatherModel.locationModel.cityName,
                        forecastDateString = weatherModel.shortDateString
                    )
                )
            }
            is DataState.Error -> {
                DataState.Error.buildWidgetError(
                    errorTitle = weatherModelDataState.errorDescription
                )
            }
            is DataState.Loading -> {
                DataState.Error.buildWidgetError(
                    errorTitle = weatherModelDataState.message
                        ?: "При запросе данных о погоде произошла ошибка."
                )
            }
        }
    }

    fun observableData(): Observable<DataState<WidgetWeatherModel>> {
        return weatherTodayRepository.observableData()
            .filter { it is DataState.Success }
            .map { weatherTodayModelDataState: DataState<WeatherTodayModel> ->
                weatherTodayModelDataState as DataState.Success
                val weatherModel = weatherTodayModelDataState.data
                DataState.Success(
                    data = WidgetWeatherModel(
                        weatherIconId = weatherModel.iconId,
                        temperature = weatherModel.temperature,
                        locationDescription = weatherModel.locationModel.cityName,
                        forecastDateString = weatherModel.shortDateString
                    )
                )
            }
    }
}