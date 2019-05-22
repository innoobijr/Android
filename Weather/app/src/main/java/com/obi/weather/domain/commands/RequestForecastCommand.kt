package com.obi.weather.domain.commands

import com.obi.weather.data.ForecastResult
import com.obi.weather.domain.datasource.ForecastProvider
import com.obi.weather.domain.model.Forecast

class RequestForecastCommand(val id: Long,
                             private val forecastProvider: ForecastProvider = ForecastProvider())
    : Command<Forecast>{

    /*companion object {
        const val DAYS = 7
    }*/
    override fun execute() = forecastProvider.requestForecast(id)
}