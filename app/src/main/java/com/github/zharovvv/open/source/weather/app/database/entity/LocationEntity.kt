package com.github.zharovvv.open.source.weather.app.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_table")
data class LocationEntity(
    @PrimaryKey
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    @ColumnInfo(name = "city_name")
    val cityName: String,
    @ColumnInfo(name = "country_name")
    val countryName: String
)
