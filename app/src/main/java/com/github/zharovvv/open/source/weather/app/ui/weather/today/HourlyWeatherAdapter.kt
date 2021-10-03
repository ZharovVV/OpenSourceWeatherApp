package com.github.zharovvv.open.source.weather.app.ui.weather.today

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.databinding.ListItemHourlyWeatherBinding
import com.github.zharovvv.open.source.weather.app.model.HourlyWeatherItemModel
import com.github.zharovvv.open.source.weather.app.util.FirstValueIndicatorWrapper
import com.github.zharovvv.open.source.weather.app.util.TimeIndicator
import com.github.zharovvv.open.source.weather.app.util.withFirstValueIndicatorWrapper

class HourlyWeatherAdapter :
    ListAdapter<HourlyWeatherItemModel, HourlyWeatherAdapter.HourlyWeatherViewHolder>(
        DIFF_UTIL_ITEM_CALLBACK
    ) {

    companion object {
        private val DIFF_UTIL_ITEM_CALLBACK =
            object : DiffUtil.ItemCallback<HourlyWeatherItemModel>() {
                override fun areItemsTheSame(
                    oldItem: HourlyWeatherItemModel,
                    newItem: HourlyWeatherItemModel
                ): Boolean {
                    return oldItem.timeString == newItem.timeString
                            && oldItem.timeIndicator == newItem.timeIndicator
                }

                override fun areContentsTheSame(
                    oldItem: HourlyWeatherItemModel,
                    newItem: HourlyWeatherItemModel
                ): Boolean {
                    return oldItem == newItem
                }

            }
    }

    class HourlyWeatherViewHolder(
        private val binding: ListItemHourlyWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hourlyWeatherItemModel: HourlyWeatherItemModel) {
            with(binding) {
                hourlyWeatherCardView.isSelected = hourlyWeatherItemModel.now
                hourlyWeatherItemTimeTextView.text = hourlyWeatherItemModel.timeString
                hourlyWeatherItemIconImageView.setImageResource(hourlyWeatherItemModel.iconId)
                hourlyWeatherItemValueTextView.text = hourlyWeatherItemModel.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        return HourlyWeatherViewHolder(
            ListItemHourlyWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HourlyWeatherItemOnScrollListener : RecyclerView.OnScrollListener() {
    private val _timeIndicatorData = MutableLiveData<TimeIndicator>()
    val timeIndicatorData: LiveData<FirstValueIndicatorWrapper<TimeIndicator>> =
        Transformations.distinctUntilChanged(_timeIndicatorData).withFirstValueIndicatorWrapper()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val firstItemPosition = getFirstVisibleItemPosition(recyclerView)
        val currentAdapter = recyclerView.adapter as HourlyWeatherAdapter
        currentAdapter.currentList[firstItemPosition]?.let {
            _timeIndicatorData.value = it.timeIndicator
        }
    }

    private fun getFirstVisibleItemPosition(recyclerView: RecyclerView): Int {
        val currentLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        return currentLayoutManager.findFirstVisibleItemPosition()
    }
}