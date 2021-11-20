package com.github.zharovvv.open.source.weather.app.repository

import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.database.dao.WeatherTodayDao
import com.github.zharovvv.open.source.weather.app.database.entity.WeatherTodayEntity
import com.github.zharovvv.open.source.weather.app.model.LocationModel
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import com.github.zharovvv.open.source.weather.app.network.dto.CurrentWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.distanceBetween
import retrofit2.Call

class WeatherTodayRepository(
    private val weatherTodayDao: WeatherTodayDao,
    private val weatherApiService: WeatherApiService,
    weatherTodayConverter: WeatherTodayConverter
) : BaseObservableRepository<CurrentWeatherResponse, WeatherTodayEntity, WeatherTodayModel>(
    observableDataFromDatabase = weatherTodayDao.getWeatherToday(),
    converter = weatherTodayConverter
) {

    override fun getLastKnownDataFromDatabase(): WeatherTodayEntity? {
        return weatherTodayDao.getLastKnownWeatherToday()
    }

    override fun shouldFetchData(
        lastKnownEntity: WeatherTodayEntity?,
        newLocationModel: LocationModel
    ): Boolean {
        return lastKnownEntity == null || distanceBetween(
            oldLat = lastKnownEntity.latitude,
            oldLon = lastKnownEntity.longitude,
            newLat = newLocationModel.latitude,
            newLon = newLocationModel.longitude
        ) > 2000f || !lastKnownEntity.isFresh
    }

    override fun callApiService(lat: Float, lon: Float): Call<CurrentWeatherResponse> {
        return weatherApiService.getCurrentWeatherByCoordinates(
            lat,
            lon,
            BuildConfig.WEATHER_API_KEY
        )
    }

    override fun insertDataToDatabase(newEntity: WeatherTodayEntity) {
        weatherTodayDao.insertWeatherToday(newEntity)
    }

    override fun updateDataInDatabase(entity: WeatherTodayEntity) {
        weatherTodayDao.updateWeatherToday(entity)
    }

}