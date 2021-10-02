package com.github.zharovvv.open.source.weather.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.zharovvv.open.source.weather.app.database.dao.HourlyWeatherDao
import com.github.zharovvv.open.source.weather.app.database.dao.LocationDao
import com.github.zharovvv.open.source.weather.app.database.dao.WeatherTodayDao
import com.github.zharovvv.open.source.weather.app.database.entity.HourlyWeatherEntity
import com.github.zharovvv.open.source.weather.app.database.entity.LocationEntity
import com.github.zharovvv.open.source.weather.app.database.entity.WeatherTodayEntity

@Database(
    entities = [LocationEntity::class, WeatherTodayEntity::class, HourlyWeatherEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun weatherTodayDao(): WeatherTodayDao

    abstract fun hourlyWeatherDao(): HourlyWeatherDao
}