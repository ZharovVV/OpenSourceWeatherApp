package com.github.zharovvv.open.source.weather.app.repository

import android.location.Location
import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.database.dao.WeatherTodayDao
import com.github.zharovvv.open.source.weather.app.database.entity.WeatherTodayEntity
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.WeatherTodayModel
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import com.github.zharovvv.open.source.weather.app.network.dto.CurrentWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.isFresh
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response
import java.io.IOException

class WeatherTodayRepository {

    private val weatherTodayDao: WeatherTodayDao =
        OpenSourceWeatherApp.appDatabase.weatherTodayDao()
    private val weatherApiService: WeatherApiService = OpenSourceWeatherApp.weatherApiService
    private val weatherTodayConverter = WeatherTodayConverter()
    private val behaviorSubject: BehaviorSubject<DataState<WeatherTodayModel>> =
        BehaviorSubject.createDefault(DataState.Loading())

    fun weatherTodayObservable(compositeDisposable: CompositeDisposable): Observable<DataState<WeatherTodayModel>> {
        compositeDisposable += weatherTodayDao.getWeatherToday()
            .filter { entity -> entity.isFresh }
            .map { entity -> weatherTodayConverter.convertToModel(entity) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { behaviorSubject.onNext(DataState.Success(it)) },
                { behaviorSubject.onNext(DataState.Error(it.message ?: "")) }
            )
        return behaviorSubject
    }

    private val WeatherTodayEntity.isFresh: Boolean get() = time.isFresh(freshPeriodInMinutes = 2)

    fun requestTodayWeather(lat: Float, lon: Float, withLoadingStatus: Boolean = true) {
        val previousValue = behaviorSubject.value
        if (withLoadingStatus) {
            behaviorSubject.onNext(DataState.Loading())
        }
        val weatherTodayEntity: WeatherTodayEntity? = weatherTodayDao.getLastKnownWeatherToday()
        if (shouldFetchData(weatherTodayEntity, lat, lon)) {
            try {
                fetchData(weatherTodayEntity, lat, lon)
            } catch (e: IOException) {
                behaviorSubject.onNext(DataState.Error(e.message ?: ""))
                return
            }
        } else {
            if (withLoadingStatus) {
                behaviorSubject.onNext(previousValue!!)
            }
        }
    }

    private fun fetchData(weatherTodayEntity: WeatherTodayEntity?, lat: Float, lon: Float) {
//        Thread.sleep(3000L) //TODO test loading
        val response: Response<CurrentWeatherResponse> =
            weatherApiService.getCurrentWeatherByCoordinates(
                lat,
                lon,
                BuildConfig.WEATHER_API_KEY
            ).execute()
        if (response.isSuccessful) {
            val currentWeatherResponse = response.body()!!
            val newWeatherTodayEntity = weatherTodayConverter.convertToEntity(
                entityId = weatherTodayEntity?.id ?: 0,
                latitude = lat,
                longitude = lon,
                response = currentWeatherResponse
            )
            if (weatherTodayEntity == null) {
                weatherTodayDao.insertWeatherToday(newWeatherTodayEntity)
            } else {
                weatherTodayDao.updateWeatherToday(newWeatherTodayEntity)
            }
        } else {
            behaviorSubject.onNext(DataState.Error(response.message()))
        }
    }

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
        ) || !weatherTodayEntity.isFresh
    }

}