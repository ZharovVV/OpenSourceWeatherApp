package com.github.zharovvv.open.source.weather.app.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private var _isRestoredFromBackStack = false
    protected val isRestoredFromBackStack get() = _isRestoredFromBackStack
    protected val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("FRAGMENT_LIFECYCLE", "$this#onCreate")
        _isRestoredFromBackStack = false
    }

    //onCreateView

    //onViewCreated

    override fun onStart() {
        super.onStart()
        Log.d("FRAGMENT_LIFECYCLE", "$this#onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("FRAGMENT_LIFECYCLE", "$this#onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("FRAGMENT_LIFECYCLE", "$this#onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("FRAGMENT_LIFECYCLE", "$this#onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("FRAGMENT_LIFECYCLE", "$this#onDestroyView")
        _isRestoredFromBackStack = true
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("FRAGMENT_LIFECYCLE", "$this#onDestroy")
    }
}