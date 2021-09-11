package com.github.zharovvv.open.source.weather.app.ui.weather.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.databinding.ListItemDetailedWeatherParamsBinding
import com.github.zharovvv.open.source.weather.app.model.DetailedWeatherParamModel

class DetailedWeatherParamsAdapter :
    ListAdapter<DetailedWeatherParamModel, DetailedWeatherParamsAdapter.DetailedWeatherParamsViewHolder>(
        DIFF_UTIL_ITEM_CALLBACK
    ) {

    companion object {
        private val DIFF_UTIL_ITEM_CALLBACK =
            object : DiffUtil.ItemCallback<DetailedWeatherParamModel>() {
                override fun areItemsTheSame(
                    oldItem: DetailedWeatherParamModel,
                    newItem: DetailedWeatherParamModel
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: DetailedWeatherParamModel,
                    newItem: DetailedWeatherParamModel
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    class DetailedWeatherParamsViewHolder(
        private val binding: ListItemDetailedWeatherParamsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(detailedWeatherParamModel: DetailedWeatherParamModel) {
            with(binding) {
                detailedWeatherParamsNameTextView.text = detailedWeatherParamModel.name
                detailedWeatherParamsIconImageView.setImageResource(detailedWeatherParamModel.iconId)
                detailedWeatherParamsValueTextView.text = detailedWeatherParamModel.value
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailedWeatherParamsViewHolder {
        return DetailedWeatherParamsViewHolder(
            ListItemDetailedWeatherParamsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailedWeatherParamsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}