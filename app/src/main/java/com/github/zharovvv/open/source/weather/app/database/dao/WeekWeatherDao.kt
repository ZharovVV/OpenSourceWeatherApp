package com.github.zharovvv.open.source.weather.app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.database.entity.WeekWeatherEntity
import io.reactivex.Observable

@Dao
interface WeekWeatherDao {
    @Insert
    fun insertWeekWeather(weekWeatherEntity: WeekWeatherEntity)

    @Update
    fun updateWeekWeather(weekWeatherEntity: WeekWeatherEntity)

    @Query("SELECT * FROM week_weather_table")
    fun getWeekWeather(): Observable<WeekWeatherEntity>

    @Query("SELECT * FROM week_weather_table LIMIT 1")
    fun getLastKnownWeekWeather(): WeekWeatherEntity?
}