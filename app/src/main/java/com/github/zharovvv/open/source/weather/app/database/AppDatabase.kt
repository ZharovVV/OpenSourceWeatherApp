package com.github.zharovvv.open.source.weather.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.zharovvv.open.source.weather.app.database.dao.LocationDao
import com.github.zharovvv.open.source.weather.app.database.entity.LocationEntity

@Database(entities = [LocationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao
}