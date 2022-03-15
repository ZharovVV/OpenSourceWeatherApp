package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.data.local.HourlyWeatherDao
import com.github.zharovvv.open.source.weather.app.data.remote.WeatherApiGateway
import com.github.zharovvv.open.source.weather.app.domain.IHourlyWeatherRepository
import com.github.zharovvv.open.source.weather.app.models.data.local.HourlyWeatherEntity
import com.github.zharovvv.open.source.weather.app.models.data.remote.HourlyWeatherResponse
import com.github.zharovvv.open.source.weather.app.models.presentation.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.util.distanceBetween
import retrofit2.Call

class HourlyWeatherRepository(
    private val hourlyWeatherDao: HourlyWeatherDao,
    private val weatherApiGateway: WeatherApiGateway,
    hourlyWeatherConverter: HourlyWeatherConverter,
) : BaseObservableRepository<HourlyWeatherResponse, HourlyWeatherEntity, HourlyWeatherModel>(
    observableDataFromDatabase = hourlyWeatherDao.getHourlyWeather(),
    converter = hourlyWeatherConverter
), IHourlyWeatherRepository {

    override fun getLastKnownDataFromDatabase(): HourlyWeatherEntity? {
        return hourlyWeatherDao.getLastKnownHourlyWeather()
    }

    override fun shouldFetchData(
        lastKnownEntity: HourlyWeatherEntity?,
        newLocationModel: LocationModel,
    ): Boolean {
        return lastKnownEntity == null || distanceBetween(
            oldLat = lastKnownEntity.latitude,
            oldLon = lastKnownEntity.longitude,
            newLat = newLocationModel.latitude,
            newLon = newLocationModel.longitude
        ) > 2000f || !lastKnownEntity.isFresh
    }

    override fun callApiService(lat: Float, lon: Float): Call<HourlyWeatherResponse> {
        return weatherApiGateway.getHourlyWeatherByCoordinates(lat, lon)
    }

    override fun insertDataToDatabase(newEntity: HourlyWeatherEntity) {
        hourlyWeatherDao.insertHourlyWeather(newEntity)
    }

    override fun updateDataInDatabase(entity: HourlyWeatherEntity) {
        hourlyWeatherDao.updateHourlyWeather(entity)
    }
}