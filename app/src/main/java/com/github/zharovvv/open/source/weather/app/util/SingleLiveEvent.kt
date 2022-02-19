package com.github.zharovvv.open.source.weather.app.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class MutableSingleLiveEvent<T> : MutableLiveData<T>(), SingleLiveEvent<T> {

    private val mPending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t: T ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    override fun setValue(value: T) {
        mPending.set(true)
        super.setValue(value)
    }
}

/**
 * LiveData, присылающая значение подписчику только один раз (вне зависимости от смены конфигурации).
 */
interface SingleLiveEvent<T> {

    fun observe(owner: LifecycleOwner, observer: Observer<in T>)
}