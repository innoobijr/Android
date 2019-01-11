package com.obi.weatherapp.domain.commands

import com.obi.weatherapp.data.ForecastRequest
import com.obi.weatherapp.domain.Command
import com.obi.weatherapp.domain.mappers.ForecastDataMapper
import com.obi.weatherapp.domain.model.ForecastList

class RequestForecastCommand(private val zipCode: String) :
        Command<ForecastList> {
    override fun execute(): ForecastList {
        val forecastRequest = ForecastRequest(zipCode)
        return ForecastDataMapper().convertFromDataModel(
                forecastRequest.execute()
        )
    }

}