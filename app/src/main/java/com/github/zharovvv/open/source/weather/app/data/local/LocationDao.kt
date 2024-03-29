package com.github.zharovvv.open.source.weather.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.zharovvv.open.source.weather.app.models.data.local.LocationEntity
import io.reactivex.Flowable

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(locationEntity: LocationEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLocation(locationEntity: LocationEntity)

    @Query("SELECT * FROM location_table")
    fun getLocation(): Flowable<LocationEntity>

    @Query("SELECT * FROM location_table LIMIT 1")
    fun getLastKnownLocation(): LocationEntity?
}