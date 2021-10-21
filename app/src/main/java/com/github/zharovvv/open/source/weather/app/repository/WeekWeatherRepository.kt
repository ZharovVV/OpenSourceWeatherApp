package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.database.dao.WeekWeatherDao
import com.github.zharovvv.open.source.weather.app.database.entity.WeekWeatherEntity
import com.github.zharovvv.open.source.weather.app.model.WeekWeatherModel
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import com.github.zharovvv.open.source.weather.app.network.dto.WeekWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.distanceBetween
import retrofit2.Call

class WeekWeatherRepository(
    private val weekWeatherDao: WeekWeatherDao,
    private val weatherApiService: WeatherApiService,
    weekWeatherConverter: WeekWeatherConverter
) : BaseObservableRepository<WeekWeatherResponse, WeekWeatherEntity, WeekWeatherModel>(
    observableDataFromDatabase = weekWeatherDao.getWeekWeather(),
    converter = weekWeatherConverter
) {

    override fun getLastKnownDataFromDatabase(): WeekWeatherEntity? {
        return weekWeatherDao.getLastKnownWeekWeather()
    }

    override fun shouldFetchData(
        lastKnownEntity: WeekWeatherEntity?,
        newLat: Float,
        newLon: Float
    ): Boolean {
        return lastKnownEntity == null || distanceBetween(
            lastKnownEntity.latitude,
            lastKnownEntity.longitude,
            newLat,
            newLon
        ) > 2000f || !lastKnownEntity.isFresh
    }

    override fun callApiService(lat: Float, lon: Float): Call<WeekWeatherResponse> {
        return weatherApiService.getWeekWeatherByCoordinates(
            lat,
            lon,
            BuildConfig.WEATHER_API_KEY
        )
    }

    override fun insertDataToDatabase(newEntity: WeekWeatherEntity) {
        weekWeatherDao.insertWeekWeather(newEntity)
    }

    override fun updateDataInDatabase(entity: WeekWeatherEntity) {
        weekWeatherDao.updateWeekWeather(entity)
    }
}