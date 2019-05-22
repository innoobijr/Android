package com.obi.weather.data.db

import com.obi.weather.domain.model.ForecastList
import com.obi.weather.ui.utils.toVarargArray
import com.obi.weather.ui.utils.clear
import com.obi.weather.ui.utils.parseList
import com.obi.weather.ui.utils.parseOpt
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.insert
import java.util.*

class ForecastDb (
        private val forecastDbHelper: ForecastDbHelper = ForecastDbHelper.instance,
        private val dataMapper: DbDataMapper = DbDataMapper()) {

    fun requestForecastByZipCode(zipCode: Long, date: Long) = forecastDbHelper.use {
        val dailyRequest = "${DayForecastTable.CITY_ID} = {id} " + "AND ${DayForecastTable.DATE} >= {date}"

        val dailyForecast = select(DayForecastTable.NAME)
                .where(dailyRequest,"id" to zipCode, "date" to date)
                .parseList{ DayForecast(HashMap(it))}
        val city = select(CityForecastTable.NAME)
                .whereSimple("${CityForecastTable.ID} = ?", zipCode.toString())
                .parseOpt { CityForecast(HashMap(it), dailyForecast)}

        if(city != null) dataMapper.convertToDomain(city) else null


    }

    fun saveForecast(forecast: ForecastList) = forecastDbHelper.use {

        clear(CityForecastTable.NAME)
        clear(DayForecastTable.NAME)

        with(dataMapper.convertFromDomain(forecast)) {
            insert(CityForecastTable.NAME, *map.toVarargArray())
            dailyForecast.forEach {
                insert(DayForecastTable.NAME, *it.map.toVarargArray())
            }
        }

    }

}

