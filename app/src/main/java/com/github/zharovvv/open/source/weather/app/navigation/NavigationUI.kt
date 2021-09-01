package com.github.zharovvv.open.source.weather.app.navigation

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

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