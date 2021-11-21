package com.github.zharovvv.open.source.weather.app.ui.about.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.zharovvv.open.source.weather.app.model.AboutAppModel
import com.github.zharovvv.open.source.weather.app.repository.AboutAppRepositoryProvider

class AboutAppViewModel : ViewModel() {

    private val aboutAppRepository = AboutAppRepositoryProvider.aboutAppRepository
    val aboutAppData: LiveData<AboutAppModel> = MutableLiveData(aboutAppRepository.requestData())
}