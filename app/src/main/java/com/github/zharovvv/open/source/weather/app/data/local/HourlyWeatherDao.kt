package com.github.zharovvv.open.source.weather.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.models.data.local.HourlyWeatherEntity
import io.reactivex.Observable

@Dao
interface HourlyWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHourlyWeather(hourlyWeatherEntity: HourlyWeatherEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateHourlyWeather(hourlyWeatherEntity: HourlyWeatherEntity)

    @Query("SELECT * FROM hourly_weather_table")
    fun getHourlyWeather(): Observable<HourlyWeatherEntity>

    @Query("SELECT * FROM hourly_weather_table LIMIT 1")
    fun getLastKnownHourlyWeather(): HourlyWeatherEntity?
}