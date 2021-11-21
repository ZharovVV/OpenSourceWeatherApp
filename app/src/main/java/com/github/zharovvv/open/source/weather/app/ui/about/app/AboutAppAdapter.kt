package com.github.zharovvv.open.source.weather.app.ui.about.app

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.databinding.ListItemAboutAppBinding
import com.github.zharovvv.open.source.weather.app.model.AboutAppParameter
import com.github.zharovvv.open.source.weather.app.util.getColorFromAttr

class AboutAppAdapter : ListAdapter<AboutAppParameter, AboutAppAdapter.AboutAppViewHolder>(
    diffUtilItemCallback
) {

    companion object {
        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<AboutAppParameter>() {

            override fun areItemsTheSame(
                oldItem: AboutAppParameter,
                newItem: AboutAppParameter
            ): Boolean {
                return oldItem.parameterName == newItem.parameterName
            }

            override fun areContentsTheSame(
                oldItem: AboutAppParameter,
                newItem: AboutAppParameter
            ): Boolean {
                return oldItem == newItem
            }

        }
    }

    class AboutAppViewHolder(
        private val binding: ListItemAboutAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            with(binding.aboutAppParameterValueTextView) {
                movementMethod = LinkMovementMethod.getInstance()
                setLinkTextColor(context.getColorFromAttr(R.attr.colorOnPrimarySurface))
            }
        }

        fun bind(model: AboutAppParameter) {
            with(binding) {
                aboutAppParameterNameTextView.text = model.parameterName
                aboutAppParameterValueTextView.text = model.parameterValue
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutAppViewHolder {
        return AboutAppViewHolder(
            binding = ListItemAboutAppBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AboutAppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}