package com.github.zharovvv.open.source.weather.app.presentation.about.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.data.repositories.AboutAppRepositoryProvider
import com.github.zharovvv.open.source.weather.app.models.presentation.AboutAppModel

class AboutAppViewModel : ViewModel() {

    private val aboutAppRepository = AboutAppRepositoryProvider.aboutAppRepository
    val aboutAppData: LiveData<AboutAppModel> = MutableLiveData(aboutAppRepository.requestData())
}