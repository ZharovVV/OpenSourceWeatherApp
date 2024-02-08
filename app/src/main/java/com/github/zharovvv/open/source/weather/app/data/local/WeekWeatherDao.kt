package com.github.zharovvv.open.source.weather.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.models.data.local.WeekWeatherEntity
import io.reactivex.Observable

@Dao
interface WeekWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeekWeather(weekWeatherEntity: WeekWeatherEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWeekWeather(weekWeatherEntity: WeekWeatherEntity)

    @Query("SELECT * FROM week_weather_table")
    fun getWeekWeather(): Observable<WeekWeatherEntity>

    @Query("SELECT * FROM week_weather_table LIMIT 1")
    fun getLastKnownWeekWeather(): WeekWeatherEntity?
}