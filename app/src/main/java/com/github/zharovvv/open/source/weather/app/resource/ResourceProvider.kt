package com.github.zharovvv.open.source.weather.app.resource

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import com.github.zharovvv.open.source.weather.app.di.AppContext
import com.github.zharovvv.open.source.weather.app.di.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class ResourceProvider @Inject constructor(
    @AppContext
    private val applicationContext: Context
) {

    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return applicationContext.resources.getStringArray(id)
    }

    fun getString(@StringRes resId: Int): String {
        return applicationContext.getString(resId)
    }
}