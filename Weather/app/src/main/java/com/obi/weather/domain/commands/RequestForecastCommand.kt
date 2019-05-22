package com.obi.weather.domain.commands

import com.obi.weather.data.ForecastRequest
import com.obi.weather.data.ForecastResult
import com.obi.weather.domain.mappers.ForecastDataMapper
import com.obi.weather.domain.model.ForecastList

class RequestForecastCommand(private val zipCode: String) :
Command<ForecastList>{
    override fun execute(): ForecastList {
        val forecastRequest = ForecastRequest(zipCode)
        return ForecastDataMapper().convertFromDataModel(
                forecastRequest.execute())
    }
}