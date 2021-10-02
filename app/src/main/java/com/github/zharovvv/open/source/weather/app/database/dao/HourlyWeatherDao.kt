package com.github.zharovvv.open.source.weather.app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherEntity
import io.reactivex.Observable

@Dao
interface HourlyWeatherDao {
    @Insert
    fun insertHourlyWeather(hourlyWeatherEntity: HourlyWeatherEntity)

    @Update
    fun updateHourlyWeather(hourlyWeatherEntity: HourlyWeatherEntity)

    @Query("SELECT * FROM hourly_weather_table")
    fun getHourlyWeather(): Observable<HourlyWeatherEntity>

    @Query("SELECT * FROM hourly_weather_table LIMIT 1")
    fun getLastKnownHourlyWeather(): HourlyWeatherEntity?
}