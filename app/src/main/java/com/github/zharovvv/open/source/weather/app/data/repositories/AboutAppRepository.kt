package com.github.zharovvv.open.source.weather.app.data.repositories

import android.content.Context
import android.text.Html
import com.github.zharovvv.open.source.weather.app.BuildConfig
import com.github.zharovvv.open.source.weather.app.R
import com.github.zharovvv.open.source.weather.app.domain.IAboutAppRepository
import com.github.zharovvv.open.source.weather.app.models.presentation.AboutAppModel
import com.github.zharovvv.open.source.weather.app.models.presentation.AboutAppParameter

class AboutAppRepository(
    appContext: Context
) : IAboutAppRepository {

    private val aboutAppModel = AboutAppModel(
        parameters = listOf(
            AboutAppParameter(
                parameterName = appContext.getString(R.string.about_app_version_parameter_name),
                parameterValue = BuildConfig.VERSION_NAME
            ),
            AboutAppParameter(
                parameterName = appContext.getString(R.string.about_app_source_data_parameter_name),
                parameterValue = Html.fromHtml(
                    appContext.getString(R.string.about_app_source_data_parameter_value)
                )
            ),
            AboutAppParameter(
                parameterName = appContext.getString(R.string.about_app_source_code_parameter_name),
                parameterValue = Html.fromHtml(
                    appContext.getString(R.string.about_app_source_code_parameter_value)
                )
            )
        )
    )

    override fun requestData(): AboutAppModel {
        return aboutAppModel
    }
}