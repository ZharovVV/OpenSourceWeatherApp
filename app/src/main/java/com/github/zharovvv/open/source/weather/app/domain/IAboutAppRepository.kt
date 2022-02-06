package com.github.zharovvv.open.source.weather.app.domain

import com.github.zharovvv.open.source.weather.app.models.presentation.AboutAppModel

interface IAboutAppRepository {
    fun requestData(): AboutAppModel
}