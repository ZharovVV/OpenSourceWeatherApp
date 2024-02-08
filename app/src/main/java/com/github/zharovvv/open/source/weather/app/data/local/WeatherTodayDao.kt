package com.github.zharovvv.open.source.weather.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.models.data.local.WeatherTodayEntity
import io.reactivex.Observable

@Dao
interface WeatherTodayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeatherToday(weatherTodayEntity: WeatherTodayEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWeatherToday(weatherTodayEntity: WeatherTodayEntity)

    @Query("SELECT * FROM weather_today_table")
    fun getWeatherToday(): Observable<WeatherTodayEntity>

    @Query("SELECT * FROM weather_today_table LIMIT 1")
    fun getLastKnownWeatherToday(): WeatherTodayEntity?
}