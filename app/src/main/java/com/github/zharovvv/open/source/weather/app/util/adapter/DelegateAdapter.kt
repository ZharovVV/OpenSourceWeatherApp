package com.github.zharovvv.open.source.weather.app.util.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.models.presentation.DelegateAdapterItem

abstract class DelegateAdapter<M : DelegateAdapterItem, VH : RecyclerView.ViewHolder>(val modelClass: Class<out M>) {
    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindViewHolder(model: M, viewHolder: VH)
}