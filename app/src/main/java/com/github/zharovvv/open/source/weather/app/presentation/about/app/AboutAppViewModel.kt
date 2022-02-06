package com.github.zharovvv.open.source.weather.app.presentation.about.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.domain.IAboutAppRepository
import com.github.zharovvv.open.source.weather.app.models.presentation.AboutAppModel

class AboutAppViewModel(
    private val aboutAppRepository: IAboutAppRepository
) : ViewModel() {
    val aboutAppData: LiveData<AboutAppModel> = MutableLiveData(aboutAppRepository.requestData())
}