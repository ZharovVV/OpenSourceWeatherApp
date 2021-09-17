package com.github.zharovvv.open.source.weather.app.util

import android.app.Application
import android.app.Service
import android.content.Intent

inline fun <reified S : Service> Application.startService() {
    val intent = Intent(this, S::class.java)
    startService(intent)
}

inline fun <reified S : Service> Application.stopService() {
    val intent = Intent(this, S::class.java)
    stopService(intent)
}