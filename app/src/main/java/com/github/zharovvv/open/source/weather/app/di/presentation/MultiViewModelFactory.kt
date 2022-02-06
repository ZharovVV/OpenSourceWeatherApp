package com.github.zharovvv.open.source.weather.app.di.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class MultiViewModelFactory
@Inject constructor(
    private val viewModelFactoriesMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    //Используем здесь именно Provider так как каждый раз нам нужен новый инстанс ViewModel-и.
) : ViewModelProvider.Factory {

    val viewModelClasses: Set<Class<out ViewModel>> get() = viewModelFactoriesMap.keys

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return viewModelFactoriesMap.getValue(modelClass as Class<ViewModel>).get() as T
    }
}