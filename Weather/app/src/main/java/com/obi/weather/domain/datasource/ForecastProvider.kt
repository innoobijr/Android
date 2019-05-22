package com.obi.weather.domain.datasource

import com.obi.weather.data.db.ForecastDb
import com.obi.weather.data.server.ForecastServer
import com.obi.weather.domain.model.Forecast
import com.obi.weather.domain.model.ForecastList
import com.obi.weather.ui.utils.firstResult

class ForecastProvider(private val sources: List<ForecastDataSource> = ForecastProvider.SOURCES) {

    companion object {
        const val DAY_IN_MILIS = 1000 * 60 * 60 * 24
        val SOURCES = listOf(ForecastDb(), ForecastServer())
    }

    fun requestByZipCode(zipCode: Long, days: Int): ForecastList = requestToSources {
        val res = it.requestForecastByZipCode(zipCode, todayTimeSpan())
        if (res != null && res.size >= days) res else null
    }
    fun requestForecast(id: Long): Forecast  = requestToSources {
        it.requestDayForecast(id)
    }

    private fun todayTimeSpan() = System.currentTimeMillis() /
            DAY_IN_MILIS * DAY_IN_MILIS

    private fun <T : Any> requestToSources(f: (ForecastDataSource) -> T?): T =
            sources.firstResult { f(it) }
}