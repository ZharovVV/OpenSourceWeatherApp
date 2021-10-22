package com.github.zharovvv.open.source.weather.app.ui.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.bottomnavigation.BottomNavigationView

class CustomBottomNavigationView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    internal lateinit var sourceItemSelectedListener: OnItemSelectedListener

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        sourceItemSelectedListener = listener!!
        super.setOnItemSelectedListener(listener)
    }
}