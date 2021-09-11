package com.github.zharovvv.open.source.weather.app.ui.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.navigation.NavigationView

class CustomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NavigationView(context, attrs, defStyleAttr) {

    internal lateinit var sourceOnNavigationItemSelectedListener: OnNavigationItemSelectedListener

    override fun setNavigationItemSelectedListener(listener: OnNavigationItemSelectedListener?) {
        sourceOnNavigationItemSelectedListener = listener!!
        super.setNavigationItemSelectedListener(listener)
    }
}