package com.github.zharovvv.open.source.weather.app.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import com.github.zharovvv.open.source.weather.app.appComponent
import com.github.zharovvv.open.source.weather.app.di.presentation.MultiViewModelFactory
import com.github.zharovvv.open.source.weather.app.logger.Logger
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    internal lateinit var multiViewModelFactory: MultiViewModelFactory

    private var _isRestoredFromBackStack = false
    protected val isRestoredFromBackStack get() = _isRestoredFromBackStack
    protected val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        requireContext().appComponent.inject(this)
        super.onCreate(savedInstanceState)
        Logger.d("FRAGMENT_LIFECYCLE", "$this#onCreate")
        _isRestoredFromBackStack = false
    }

    //onCreateView

    //onViewCreated

    override fun onStart() {
        super.onStart()
        Logger.d("FRAGMENT_LIFECYCLE", "$this#onStart")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("FRAGMENT_LIFECYCLE", "$this#onResume")
    }

    override fun onPause() {
        super.onPause()
        Logger.d("FRAGMENT_LIFECYCLE", "$this#onPause")
    }

    override fun onStop() {
        super.onStop()
        Logger.d("FRAGMENT_LIFECYCLE", "$this#onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Logger.d("FRAGMENT_LIFECYCLE", "$this#onDestroyView")
        _isRestoredFromBackStack = true
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("FRAGMENT_LIFECYCLE", "$this#onDestroy")
    }
}