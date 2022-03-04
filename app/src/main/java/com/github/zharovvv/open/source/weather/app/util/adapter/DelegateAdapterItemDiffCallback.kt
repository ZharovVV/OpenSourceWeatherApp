package com.github.zharovvv.open.source.weather.app.util.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.github.zharovvv.open.source.weather.app.models.presentation.DelegateAdapterItem

class DelegateAdapterItemDiffCallback : DiffUtil.ItemCallback<DelegateAdapterItem>() {

    override fun areItemsTheSame(
        oldItem: DelegateAdapterItem,
        newItem: DelegateAdapterItem,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: DelegateAdapterItem,
        newItem: DelegateAdapterItem,
    ): Boolean {
        return oldItem.content == newItem.content
    }
}