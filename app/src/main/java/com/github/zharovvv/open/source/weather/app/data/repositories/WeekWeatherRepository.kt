package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.data.local.WeekWeatherDao
import com.github.zharovvv.open.source.weather.app.data.remote.WeatherApiMapper
import com.github.zharovvv.open.source.weather.app.domain.IWeekWeatherRepository
import com.github.zharovvv.open.source.weather.app.models.data.local.WeekWeatherEntity
import com.github.zharovvv.open.source.weather.app.models.data.remote.WeekWeatherResponse
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WeekWeatherModel
import com.github.zharovvv.open.source.weather.app.util.distanceBetween
import retrofit2.Call

class WeekWeatherRepository(
    private val weekWeatherDao: WeekWeatherDao,
    private val weatherApiMapper: WeatherApiMapper,
    weekWeatherConverter: WeekWeatherConverter
) : BaseObservableRepository<WeekWeatherResponse, WeekWeatherEntity, WeekWeatherModel>(
    observableDataFromDatabase = weekWeatherDao.getWeekWeather(),
    converter = weekWeatherConverter
), IWeekWeatherRepository {

    override fun getLastKnownDataFromDatabase(): WeekWeatherEntity? {
        return weekWeatherDao.getLastKnownWeekWeather()
    }

    override fun shouldFetchData(
        lastKnownEntity: WeekWeatherEntity?,
        newLocationModel: LocationModel
    ): Boolean {
        return lastKnownEntity == null || distanceBetween(
            oldLat = lastKnownEntity.latitude,
            oldLon = lastKnownEntity.longitude,
            newLat = newLocationModel.latitude,
            newLon = newLocationModel.longitude
        ) > 2000f || !lastKnownEntity.isFresh
    }

    override fun callApiService(lat: Float, lon: Float): Call<WeekWeatherResponse> {
        return weatherApiMapper.getWeekWeatherByCoordinates(
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