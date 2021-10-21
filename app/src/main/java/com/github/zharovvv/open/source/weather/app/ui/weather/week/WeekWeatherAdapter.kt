package com.github.zharovvv.open.source.weather.app.ui.weather.week

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.databinding.ListItemWeekWeatherBinding
import com.github.zharovvv.open.source.weather.app.model.WeekWeatherItemModel

class WeekWeatherAdapter :
    ListAdapter<WeekWeatherItemModel, WeekWeatherAdapter.WeekWeatherViewHolder>(
        DIFF_UTIL_ITEM_CALLBACK
    ) {

    companion object {
        private val DIFF_UTIL_ITEM_CALLBACK =
            object : DiffUtil.ItemCallback<WeekWeatherItemModel>() {
                override fun areItemsTheSame(
                    oldItem: WeekWeatherItemModel,
                    newItem: WeekWeatherItemModel
                ): Boolean {
                    return oldItem.dayOfWeekOfForecast == newItem.dayOfWeekOfForecast
                            && oldItem.forecastDate == newItem.forecastDate
                }

                override fun areContentsTheSame(
                    oldItem: WeekWeatherItemModel,
                    newItem: WeekWeatherItemModel
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    class WeekWeatherViewHolder(
        private val binding: ListItemWeekWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(weekWeatherItemModel: WeekWeatherItemModel) {
            with(binding) {
                weekWeatherItemIconImageView.setImageResource(weekWeatherItemModel.iconId)
                weekWeatherItemDayOfWeekOfForecastTextView.text =
                    weekWeatherItemModel.dayOfWeekOfForecast
                weekWeatherItemForecastDateTextView.text = weekWeatherItemModel.forecastDate
                weekWeatherItemMaxTemperatureTextView.text = weekWeatherItemModel.maxTemperature
                weekWeatherItemMinTemperatureTextView.text = weekWeatherItemModel.minTemperature
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekWeatherViewHolder {
        return WeekWeatherViewHolder(
            ListItemWeekWeatherBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WeekWeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}