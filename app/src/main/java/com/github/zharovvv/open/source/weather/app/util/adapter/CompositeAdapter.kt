package com.github.zharovvv.open.source.weather.app.util.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.models.presentation.DelegateAdapterItem

class CompositeAdapter
private constructor(
    private val delegates: List<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>>,
) : ListAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>(DelegateAdapterItemDiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val currentModelClass = getItem(position).javaClass
        for (i in delegates.indices) {
            if (delegates[i].modelClass == currentModelClass) {
                return i
            }
        }
        throw NoSuchElementException("Can not get viewType for position $position")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegates[viewType].createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val delegateAdapter = delegates[getItemViewType(position)]
        delegateAdapter.bindViewHolder(
            model = getItem(position),
            viewHolder = holder
        )
    }

    class Builder {
        private var count = 0
        private val delegates =
            mutableListOf<DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>>()

        @Suppress("UNCHECKED_CAST")
        fun add(delegateAdapter: DelegateAdapter<out DelegateAdapterItem, out RecyclerView.ViewHolder>): Builder {
            count++
            delegates += delegateAdapter as DelegateAdapter<DelegateAdapterItem, RecyclerView.ViewHolder>
            return this
        }

        fun build(): CompositeAdapter {
            require(count != 0) { "Register at least one adapter" }
            return CompositeAdapter(delegates)
        }
    }
}