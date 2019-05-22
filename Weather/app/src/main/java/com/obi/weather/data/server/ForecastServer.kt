package com.obi.weather.data.server

import com.obi.weather.data.db.ForecastDb
import com.obi.weather.domain.datasource.ForecastDataSource
import com.obi.weather.domain.model.Forecast
import com.obi.weather.domain.model.ForecastList
import java.lang.UnsupportedOperationException

class ForecastServer(
        private val dataMapper: ServerDataMapper = ServerDataMapper(),
        private val forecastDb: ForecastDb = ForecastDb())
    : ForecastDataSource {
    override fun requestForecastByZipCode(zipCode: Long, date: Long):
                ForecastList? {
            val result = ForecastByZipCodeRequest(zipCode).execute()
            val converted = dataMapper.convertToDomain(zipCode, result)
            forecastDb.saveForecast(converted)
            return forecastDb.requestForecastByZipCode(zipCode, date)
        }

    override fun requestDayForecast(id: Long): Forecast? =
            throw UnsupportedOperationException()
}