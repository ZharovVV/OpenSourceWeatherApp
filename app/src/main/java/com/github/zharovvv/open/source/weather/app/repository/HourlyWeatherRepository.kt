package com.github.zharovvv.open.source.weather.app.repository

import android.location.Location
import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.OpenSourceWeatherApp
import com.github.zharovvv.open.source.weather.app.database.dao.HourlyWeatherDao
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherEntity
import com.github.zharovvv.open.source.weather.app.model.DataState
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherModel
import com.github.zharovvv.open.source.weather.app.network.WeatherApiService
import com.github.zharovvv.open.source.weather.app.network.dto.HourlyWeatherResponse
import com.github.zharovvv.open.source.weather.app.util.isFresh
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherRepository {

    private val hourlyWeatherDao: HourlyWeatherDao =
        OpenSourceWeatherApp.appDatabase.hourlyWeatherDao()
    private val weatherApiService: WeatherApiService = OpenSourceWeatherApp.weatherApiService
    private val hourlyWeatherConverter = HourlyWeatherConverter()
    private val behaviorSubject: BehaviorSubject<DataState<HourlyWeatherModel>> =
        BehaviorSubject.createDefault(DataState.Loading())

    init {
        @Suppress("UNUSED_VARIABLE") val connection = hourlyWeatherDao.getHourlyWeather()
            .filter { entity -> entity.isFresh }
            .map { entity -> hourlyWeatherConverter.convertToModel(entity) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { behaviorSubject.onNext(DataState.Success(it)) },
                { behaviorSubject.onNext(DataState.Error(it.message ?: "")) }
            )
    }

    fun hourlyWeatherObservable(): Observable<DataState<HourlyWeatherModel>> {
        return behaviorSubject
    }

    private val HourlyWeatherEntity.isFresh: Boolean
        get() {
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return time.isFresh(freshPeriodInMinutes = 60)
                    && items.first().timeString.substringBefore(':') == simpleDateFormat.format(Date())
                .substringBefore(':')
        }

    fun requestHourlyWeather(lat: Float, lon: Float, withLoadingStatus: Boolean = true) {
        val previousValue = behaviorSubject.value
        if (withLoadingStatus) {
            behaviorSubject.onNext(DataState.Loading())
        }
        val hourlyWeatherEntity: HourlyWeatherEntity? = hourlyWeatherDao.getLastKnownHourlyWeather()
        if (shouldFetchData(hourlyWeatherEntity, lat, lon)) {
            try {
                fetchData(hourlyWeatherEntity, lat, lon)
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

    private fun fetchData(hourlyWeatherEntity: HourlyWeatherEntity?, lat: Float, lon: Float) {
//        Thread.sleep(3000L) //TODO test loading
        val response: Response<HourlyWeatherResponse> =
            weatherApiService.getHourlyWeatherByCoordinates(
                lat,
                lon,
                BuildConfig.WEATHER_API_KEY
            ).execute()
        if (response.isSuccessful) {
            val hourlyWeatherResponse = response.body()!!
            val newHourlyWeatherEntity = hourlyWeatherConverter.convertToEntity(
                entityId = hourlyWeatherEntity?.id ?: 0,
                latitude = lat,
                longitude = lon,
                response = hourlyWeatherResponse
            )
            if (hourlyWeatherEntity == null) {
                hourlyWeatherDao.insertHourlyWeather(newHourlyWeatherEntity)
            } else {
                hourlyWeatherDao.updateHourlyWeather(newHourlyWeatherEntity)
            }
        } else {
            behaviorSubject.onNext(DataState.Error(response.message()))
        }
    }

    private fun shouldFetchData(
        weatherTodayEntity: HourlyWeatherEntity?,
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