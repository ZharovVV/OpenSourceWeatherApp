package com.github.zharovvv.open.source.weather.app.presentation.choose.city

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.ListItemChooseCityBinding
import com.github.zharovvv.open.source.weather.app.models.presentation.ChooseCityItem
import com.github.zharovvv.open.source.weather.app.util.adapter.DelegateAdapter

class ChooseCityAdapter(
    private val onItemClickListener: (ChooseCityItem) -> Unit,
) : DelegateAdapter<ChooseCityItem, ChooseCityAdapter.ChooseCityViewHolder>(
    modelClass = ChooseCityItem::class.java
) {

    class ChooseCityViewHolder(
        private val binding: ListItemChooseCityBinding,
        private val onItemClickListener: (ChooseCityItem) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        private var currentItem: ChooseCityItem? = null

        init {
            binding.root.setOnClickListener {
                currentItem?.let { onItemClickListener.invoke(it) }
            }
        }

        fun bind(model: ChooseCityItem) {
            currentItem = model
            val context = binding.root.context
            with(binding) {
                chooseCityItemCityNameTextView.text =
                    context.getString(R.string.delimiter_comma, model.locationModel.cityName)
                chooseCityItemCountryNameTextView.text = model.locationModel.countryName
                chooseCityItemCurrentRealLocationMarkerImageView.isVisible =
                    model.locationModel.isRealLocation
            }
        }
    }

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ChooseCityViewHolder(
            binding = ListItemChooseCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickListener = onItemClickListener
        )
    }

    override fun bindViewHolder(model: ChooseCityItem, viewHolder: ChooseCityViewHolder) {
        viewHolder.bind(model)
    }
}