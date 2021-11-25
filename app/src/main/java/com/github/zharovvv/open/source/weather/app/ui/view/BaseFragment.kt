package com.github.zharovvv.open.source.weather.app.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var _isRestoredFromBackStack = false
    protected val isRestoredFromBackStack get() = _isRestoredFromBackStack
    protected val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _isRestoredFromBackStack = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _isRestoredFromBackStack = true
        handler.removeCallbacksAndMessages(null)
    }
}