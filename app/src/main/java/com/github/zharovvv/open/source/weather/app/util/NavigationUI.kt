package com.github.zharovvv.open.source.weather.app.util

import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

fun Toolbar.setUpWithNavControllerCustom(
    navController: NavController,
    appBarConfiguration: AppBarConfiguration
) {
    NavigationUI.setupWithNavController(
        this,
        navController,
        AppBarConfiguration(navController.graph, appBarConfiguration.openableLayout)
    )
    setNavigationOnClickListener {
        NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}