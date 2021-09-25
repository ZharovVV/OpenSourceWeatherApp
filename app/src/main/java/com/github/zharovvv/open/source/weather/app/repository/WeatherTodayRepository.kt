package com.github.zharovvv.open.source.weather.app.repository

import android.location.Location
import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.database.dao.WeatherTodayDao
import com.github.zharovvv.open.source.weather.app.database.entity.DetailedWeatherParamPojoEntity
import com.github.zharovvv.open.source.weather.app.database.entity.WeatherTodayEntity
import com.github.zharovvv.open.source.weather.app.model.DetailedWeatherParamModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import com.github.zharovvv.open.source.weather.app.network.dto.CurrentWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.isFresh
import io.reactivex.Flowable
import retrofit2.Response
import java.util.*

class WeatherTodayRepository {

    private val weatherTodayDao: WeatherTodayDao =
        OpenSourceWeatherApp.appDatabase.weatherTodayDao()
    private val weatherApiService: WeatherApiService = OpenSourceWeatherApp.weatherApiService
    private val weatherDtoToModelConverter = WeatherResponseToModelConverter()

    fun weatherTodayObservable(): Flowable<WeatherTodayModel> = weatherTodayDao.getWeatherToday()
        .filter { it.isFresh }
        .map { weatherTodayEntity: WeatherTodayEntity ->
            WeatherTodayModel(
                iconId = weatherTodayEntity.iconId,
                description = weatherTodayEntity.description,
                dateString = weatherTodayEntity.dateString,
                temperature = weatherTodayEntity.temperature,
                detailedWeatherParams = weatherTodayEntity.detailedWeatherParams.map { weatherParamPojoEntity ->
                    DetailedWeatherParamModel(
                        name = weatherParamPojoEntity.name,
                        iconId = weatherParamPojoEntity.iconId,
                        value = weatherParamPojoEntity.value
                    )
                }
            )
        }

    fun requestTodayWeather(lat: Float, lon: Float) {
        val weatherTodayEntity: WeatherTodayEntity? = weatherTodayDao.getLastKnownWeatherToday()
        if (shouldFetchData(weatherTodayEntity, lat, lon)) {
            val response: Response<CurrentWeatherResponse> =
                weatherApiService.getCurrentWeatherByCoordinates(
                    lat,
                    lon,
                    BuildConfig.WEATHER_API_KEY
                ).execute()
            val currentWeatherResponse = response.body()!!
            val weatherTodayModel = weatherDtoToModelConverter.convert(currentWeatherResponse)
            val newWeatherTodayEntity = WeatherTodayEntity(
                id = weatherTodayEntity?.id ?: 0,
                latitude = lat,
                longitude = lon,
                time = Date(),
                iconId = weatherTodayModel.iconId,
                description = weatherTodayModel.description,
                dateString = weatherTodayModel.dateString,
                temperature = weatherTodayModel.temperature,
                detailedWeatherParams = weatherTodayModel.detailedWeatherParams.map { detailedWeatherParamModel ->
                    DetailedWeatherParamPojoEntity(
                        name = detailedWeatherParamModel.name,
                        iconId = detailedWeatherParamModel.iconId,
                        value = detailedWeatherParamModel.value
                    )
                }
            )
            if (weatherTodayEntity == null) {
                weatherTodayDao.insertWeatherToday(newWeatherTodayEntity)
            } else {
                weatherTodayDao.updateWeatherToday(newWeatherTodayEntity)
            }
        }
    }

    private val WeatherTodayEntity.isFresh: Boolean get() = time.isFresh(freshPeriodInMinutes = 2)

    private fun shouldFetchData(
        weatherTodayEntity: WeatherTodayEntity?,
        newLat: Float,
        newLon: Float
    ): Boolean {
        fun checkLocation(oldLat: Float, oldLon: Float, newLat: Float, newLon: Float): Boolean {
            val floatArray = FloatArray(1)
            Location.distanceBetween(
                oldLat.toDouble(),
                oldLon.toDouble(),
                newLat.toDouble(),
                newLon.toDouble(),
                floatArray
            )
            return floatArray[0] > 2000f
        }
        return weatherTodayEntity == null || checkLocation(
            weatherTodayEntity.latitude,
            weatherTodayEntity.longitude,
            newLat,
            newLon
        ) || weatherTodayEntity.isFresh
    }

}