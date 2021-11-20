package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.database.dao.HourlyWeatherDao
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherEntity
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import com.github.zharovvv.open.source.weather.app.network.dto.HourlyWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.distanceBetween
import retrofit2.Call

class HourlyWeatherRepository(
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val weatherApiService: WeatherApiService,
    hourlyWeatherConverter: HourlyWeatherConverter
) : BaseObservableRepository<HourlyWeatherResponse, HourlyWeatherEntity, HourlyWeatherModel>(
    observableDataFromDatabase = hourlyWeatherDao.getHourlyWeather(),
    converter = hourlyWeatherConverter
) {

    override fun getLastKnownDataFromDatabase(): HourlyWeatherEntity? {
        return hourlyWeatherDao.getLastKnownHourlyWeather()
    }

    override fun shouldFetchData(
        lastKnownEntity: HourlyWeatherEntity?,
        newLocationModel: LocationModel
    ): Boolean {
        return lastKnownEntity == null || distanceBetween(
            oldLat = lastKnownEntity.latitude,
            oldLon = lastKnownEntity.longitude,
            newLat = newLocationModel.latitude,
            newLon = newLocationModel.longitude
        ) > 2000f || !lastKnownEntity.isFresh
    }

    override fun callApiService(lat: Float, lon: Float): Call<HourlyWeatherResponse> {
        return weatherApiService.getHourlyWeatherByCoordinates(
            lat,
            lon,
            BuildConfig.WEATHER_API_KEY
        )
    }

    override fun insertDataToDatabase(newEntity: HourlyWeatherEntity) {
        hourlyWeatherDao.insertHourlyWeather(newEntity)
    }

    override fun updateDataInDatabase(entity: HourlyWeatherEntity) {
        hourlyWeatherDao.updateHourlyWeather(entity)
    }
}