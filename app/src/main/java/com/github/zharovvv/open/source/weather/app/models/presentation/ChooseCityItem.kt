package com.github.zharovvv.open.source.weather.app.models.presentation

data class ChooseCityItem(
    val locationModel: LocationModel,
    val isCurrentRealLocation: Boolean = false,
) : DelegateAdapterItem {
    override val id: Any = "${locationModel.cityName}_${locationModel.countryName}"
    override val content: Any get() = this
}