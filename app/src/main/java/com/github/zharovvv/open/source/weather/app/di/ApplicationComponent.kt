package com.github.zharovvv.open.source.weather.app.di

import android.content.Context
import com.github.zharovvv.open.source.weather.app.di.data.OpenSourceWeatherAppDataModule
import com.github.zharovvv.open.source.weather.app.di.domain.OpenSourceWeatherAppDomainModule
import com.github.zharovvv.open.source.weather.app.di.presentation.OpenSourceWeatherAppPresentationModule
import com.github.zharovvv.open.source.weather.app.presentation.BaseFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Component(
    modules = [
        OpenSourceWeatherAppDataModule::class,
        OpenSourceWeatherAppDomainModule::class,
        OpenSourceWeatherAppPresentationModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withAppContext(appContext: Context): Builder
        fun build(): ApplicationComponent
    }

    fun inject(baseFragment: BaseFragment)
}

@Scope
annotation class ApplicationScope