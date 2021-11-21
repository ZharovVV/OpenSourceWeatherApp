package com.github.zharovvv.open.source.weather.app.widget

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.github.zharovvv.open.source.weather.app.repository.WidgetWeatherRepositoryProvider
import com.github.zharovvv.open.source.weather.app.widget.work.manager.schedulePeriodicWork
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

/**
 * Сервис для автообновления виджета погоды во время работы приложения.
 */
class UpdateWeatherWidgetService : Service() {

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate() {
        Log.i("ServiceLifecycle", "$this#onCreate;")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("ServiceLifecycle", "$this#onStartCommand;")
        val widgetWeatherRepository = WidgetWeatherRepositoryProvider.widgetWeatherRepository
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        compositeDisposable += widgetWeatherRepository.observableData()
            .observeOn(Schedulers.io())
            .subscribe(
                {
                    if (appWidgetManager.hasAnyWidget(applicationContext)) {
                        updateWidget(
                            context = applicationContext,
                            widgetModelDataState = it,
                            appWidgetManager = appWidgetManager
                        )
                        schedulePeriodicWork(applicationContext)
                    }
                },
                { it.printStackTrace() }
            )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        Log.i("ServiceLifecycle", "$this#onDestroy;")
        super.onDestroy()
        compositeDisposable.clear()
    }
}