package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "latitude")
    val latitude: Float,
    @ColumnInfo(name = "longitude")
    val longitude: Float,
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @ColumnInfo(name = "country_name")
    val countryName: String
)
