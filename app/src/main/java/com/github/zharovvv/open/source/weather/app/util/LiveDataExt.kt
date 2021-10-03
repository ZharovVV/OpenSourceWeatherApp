package com.github.zharovvv.open.source.weather.app.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.withFirstValueIndicatorWrapper(): LiveData<FirstValueIndicatorWrapper<T>> {
    val outLiveData = MediatorLiveData<FirstValueIndicatorWrapper<T>>()
    outLiveData.addSource(this) { t ->
        val previousValue = outLiveData.value
        outLiveData.value = FirstValueIndicatorWrapper(
            data = t,
            isFirstValue = previousValue == null
        )
    }
    return outLiveData
}

class FirstValueIndicatorWrapper<T>(val data: T, val isFirstValue: Boolean)