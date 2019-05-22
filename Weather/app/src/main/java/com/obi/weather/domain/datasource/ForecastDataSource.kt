package com.obi.weather.domain.datasource

import com.obi.weather.domain.model.Forecast
import com.obi.weather.domain.model.ForecastList

interface ForecastDataSource {
    fun requestForecastByZipCode(zipCode: Long, date: Long): ForecastList?

    fun requestDayForecast(id: Long): Forecast?

}

