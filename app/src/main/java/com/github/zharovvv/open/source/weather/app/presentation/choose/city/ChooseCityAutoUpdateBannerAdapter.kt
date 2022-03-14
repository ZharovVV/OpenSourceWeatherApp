package com.github.zharovvv.open.source.weather.app.presentation.choose.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.databinding.ListItemAutoUpdateLocationBannerBinding
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityAutoUpdateBannerItem
import com.github.zharovvv.open.source.weather.app.util.adapter.DelegateAdapter

class ChooseCityAutoUpdateBannerAdapter(
    private val onItemClickListener: () -> Unit,
) : DelegateAdapter<ChooseCityAutoUpdateBannerItem, ChooseCityAutoUpdateBannerAdapter.ChooseCityAutoUpdateBannerViewHolder>(
    modelClass = ChooseCityAutoUpdateBannerItem::class.java
) {

    class ChooseCityAutoUpdateBannerViewHolder(
        binding: ListItemAutoUpdateLocationBannerBinding,
        onItemClickListener: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClickListener.invoke()
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ChooseCityAutoUpdateBannerViewHolder(
            binding = ListItemAutoUpdateLocationBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickListener = onItemClickListener
        )
    }

    override fun bindViewHolder(
        model: ChooseCityAutoUpdateBannerItem,
        viewHolder: ChooseCityAutoUpdateBannerViewHolder,
    ) = Unit
}