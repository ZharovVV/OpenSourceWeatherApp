package com.github.zharovvv.open.source.weather.app.presentation.choose.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.databinding.ListItemAutoUpdateLocationBannerBinding
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityAutoUpdateBannerItem
import com.github.zharovvv.open.source.weather.app.util.adapter.DelegateAdapter

class ChooseCityAutoUpdateBannerAdapter :
    DelegateAdapter<ChooseCityAutoUpdateBannerItem, ChooseCityAutoUpdateBannerAdapter.ChooseCityAutoUpdateBannerViewHolder>(
        modelClass = ChooseCityAutoUpdateBannerItem::class.java
    ) {

    class ChooseCityAutoUpdateBannerViewHolder(binding: ListItemAutoUpdateLocationBannerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ChooseCityAutoUpdateBannerViewHolder(
            binding = ListItemAutoUpdateLocationBannerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun bindViewHolder(
        model: ChooseCityAutoUpdateBannerItem,
        viewHolder: ChooseCityAutoUpdateBannerViewHolder,
    ) = Unit
}