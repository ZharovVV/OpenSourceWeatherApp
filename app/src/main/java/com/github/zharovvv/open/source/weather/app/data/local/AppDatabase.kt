package com.github.zharovvv.open.source.weather.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.zharovvv.open.source.weather.app.models.data.local.HourlyWeatherEntity
import com.github.zharovvv.open.source.weather.app.models.data.local.LocationEntity
import com.github.zharovvv.open.source.weather.app.models.data.local.WeatherTodayEntity
import com.github.zharovvv.open.source.weather.app.models.data.local.WeekWeatherEntity

@Database(
    entities = [LocationEntity::class, WeatherTodayEntity::class, HourlyWeatherEntity::class, WeekWeatherEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun weatherTodayDao(): WeatherTodayDao

    abstract fun hourlyWeatherDao(): HourlyWeatherDao

    abstract fun weekWeatherDao(): WeekWeatherDao
}