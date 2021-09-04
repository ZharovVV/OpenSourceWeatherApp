package com.github.zharovvv.open.source.weather.app.navigation

import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import java.io.Serializable

fun Toolbar.setUpWithNavControllerCustom(
    navController: NavController,
    appBarConfiguration: AppBarConfiguration
) {
    //Переопределяем AppBarConfiguration
    //Drawer icon будет отображаться только для startDestination.
    NavigationUI.setupWithNavController(
        this,
        navController,
        AppBarConfiguration(navController.graph, appBarConfiguration.openableLayout)
    )
    //Используем базовую логику обработки Navigation Click (Drawer icon and Up Button)
    //Openable (например DrawerLayout) будет открыт только для topLevelDestinationIds.
    setNavigationOnClickListener {
        NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}

/**
 * Получение данных в фрагменте, из которого был показан этот диалог,
 * после извлечения диалога из BackStack-а.
 */
inline fun <reified T : Serializable> Fragment.setUpNavDialogResultCallback(
    navBackStackEntry: NavBackStackEntry,
    resultKey: String,
    crossinline resultCallback: ((result: T) -> Unit)
) {
    val observer = LifecycleEventObserver { _, event: Lifecycle.Event ->
        if (event == Lifecycle.Event.ON_RESUME
            && navBackStackEntry.savedStateHandle.contains(resultKey)
        ) {
            val result: T? = navBackStackEntry.savedStateHandle.get<T>(resultKey)
            if (result != null) {
                resultCallback(result)
            }
            navBackStackEntry.savedStateHandle.remove<T>(resultKey) //Обрабатываем результат 1 раз
        }
    }
    navBackStackEntry.lifecycle.addObserver(observer)
    viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            navBackStackEntry.lifecycle.removeObserver(observer)
        }
    })
}

fun <T : Serializable> NavController.setResultForPreviousDestination(
    resultKey: String,
    result: T
) {
    previousBackStackEntry?.savedStateHandle?.set(resultKey, result)
}