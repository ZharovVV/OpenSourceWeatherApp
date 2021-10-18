package com.github.zharovvv.open.source.weather.app.util

import android.location.Location

fun distanceBetween(oldLat: Float, oldLon: Float, newLat: Float, newLon: Float): Float {
    return distanceBetween(
        oldLat.toDouble(),
        oldLon.toDouble(),
        newLat.toDouble(),
        newLon.toDouble()
    )
}

fun distanceBetween(oldLat: Double, oldLon: Double, newLat: Double, newLon: Double): Float {
    val floatArray = FloatArray(1)
    Location.distanceBetween(
        oldLat,
        oldLon,
        newLat,
        newLon,
        floatArray
    )
    return floatArray[0]
}