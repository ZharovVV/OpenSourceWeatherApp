package com.github.zharovvv.open.source.weather.app.data.repositories

import com.github.zharovvv.open.source.weather.app.data.local.WeatherTodayDao
import com.github.zharovvv.open.source.weather.app.data.remote.WeatherApiGateway
import com.github.zharovvv.open.source.weather.app.domain.IWeatherTodayRepository
import com.github.zharovvv.open.source.weather.app.models.data.local.WeatherTodayEntity
import com.github.zharovvv.open.source.weather.app.models.data.remote.CurrentWeatherResponse
import com.github.zharovvv.open.source.weather.app.models.presentation.LocationModel
import com.github.zharovvv.open.source.weather.app.models.presentation.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.util.distanceBetween
import retrofit2.Call

class WeatherTodayRepository(
    private val weatherTodayDao: WeatherTodayDao,
    private val weatherApiGateway: WeatherApiGateway,
    weatherTodayConverter: WeatherTodayConverter,
) : BaseObservableRepository<CurrentWeatherResponse, WeatherTodayEntity, WeatherTodayModel>(
    observableDataFromDatabase = weatherTodayDao.getWeatherToday(),
    converter = weatherTodayConverter
), IWeatherTodayRepository {

    override fun getLastKnownDataFromDatabase(): WeatherTodayEntity? {
        return weatherTodayDao.getLastKnownWeatherToday()
    }

    override fun shouldFetchData(
        lastKnownEntity: WeatherTodayEntity?,
        newLocationModel: LocationModel,
    ): Boolean {
        return lastKnownEntity == null || distanceBetween(
            oldLat = lastKnownEntity.latitude,
            oldLon = lastKnownEntity.longitude,
            newLat = newLocationModel.latitude,
            newLon = newLocationModel.longitude
        ) > 2000f || !lastKnownEntity.isFresh
    }

    override fun callApiService(lat: Float, lon: Float): Call<CurrentWeatherResponse> {
        return weatherApiGateway.getCurrentWeatherByCoordinates(lat, lon)
    }

    override fun insertDataToDatabase(newEntity: WeatherTodayEntity) {
        weatherTodayDao.insertWeatherToday(newEntity)
    }

    override fun updateDataInDatabase(entity: WeatherTodayEntity) {
        weatherTodayDao.updateWeatherToday(entity)
    }

}