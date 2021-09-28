package com.github.zharovvv.open.source.weather.app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.database.entity.WeatherTodayEntity
import io.reactivex.Observable

@Dao
interface WeatherTodayDao {
    @Insert
    fun insertWeatherToday(weatherTodayEntity: WeatherTodayEntity)

    @Update
    fun updateWeatherToday(weatherTodayEntity: WeatherTodayEntity)

    @Query("SELECT * FROM weather_today_table")
    fun getWeatherToday(): Observable<WeatherTodayEntity>

    @Query("SELECT * FROM weather_today_table LIMIT 1")
    fun getLastKnownWeatherToday(): WeatherTodayEntity?
}