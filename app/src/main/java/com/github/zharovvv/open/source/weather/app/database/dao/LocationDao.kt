package com.github.zharovvv.open.source.weather.app.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.database.entity.LocationEntity
import io.reactivex.Flowable

@Dao
interface LocationDao {

    @Insert
    fun insertLocation(locationEntity: LocationEntity)

    @Update
    fun updateLocation(locationEntity: LocationEntity)

    @Query("SELECT * FROM location_table")
    fun getLocation(): Flowable<LocationEntity>

    @Query("SELECT * FROM location_table LIMIT 1")
    fun getLastKnownLocation(): LocationEntity?
}