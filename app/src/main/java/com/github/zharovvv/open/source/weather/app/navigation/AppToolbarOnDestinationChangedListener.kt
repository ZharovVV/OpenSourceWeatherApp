package com.github.zharovvv.open.source.weather.app.navigation

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import java.lang.ref.WeakReference


class AppToolbarOnDestinationChangedListener(
    appToolbar: Toolbar
) : NavController.OnDestinationChangedListener {

    private val weakReferenceToolbar: WeakReference<Toolbar> = WeakReference(appToolbar)

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val appToolbar: Toolbar? = weakReferenceToolbar.get()
        if (appToolbar == null) {
            controller.removeOnDestinationChangedListener(this)
            return
        }
        appToolbar.isVisible = !useFragmentToolbar(arguments)
    }

    private fun useFragmentToolbar(arguments: Bundle?): Boolean {
        return arguments?.getBoolean("useFragmentToolbar", false) ?: false
    }
}