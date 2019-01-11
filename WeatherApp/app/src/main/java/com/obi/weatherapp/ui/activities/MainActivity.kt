package com.obi.weatherapp.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.obi.weatherapp.ui.adapters.ForecastListAdapter
import com.obi.weatherapp.R
import com.obi.weatherapp.domain.commands.RequestForecastCommand
import com.obi.weatherapp.domain.model.Forecast
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {

    private val items  = listOf(
            "Mon 6/23 - Sunny - 31/17",
            "Tue 6/24 - Foggy - 21/8",
            "Wed 6/25 - Cloudy - 22/17",
            "Thurs 6/26 - Rainy - 18/11",
            "Fri 6/27 - Foggy - 21/10",
            "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
            "Sun 6/29 - Sunny - 20/7"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val forecastList = findViewById(R.id.forcast_list) as RecyclerView
        forecastList.layoutManager = LinearLayoutManager(this)
        //forecastList.adapter = ForecastListAdapter(items)

        val url = "http://api.openweathermap.org/data/2.5/forecast/daily?" +
                "APPID=15646a06818f61f7b8d7823ca833e1ce&zip=94043&mode=json&units=metric&cnt=7"
        //val executor = Executors.newScheduledThreadPool(4)
        doAsync {
            val result = RequestForecastCommand("94043").execute()
            uiThread {
                val adapter = ForecastListAdapter(result){forecast ->  toast(forecast.date)}
                /*forecastList.adapter = ForecastListAdapter(result,
                        object : ForecastListAdapter.OnItemClickListener{
                            override fun invoke(forecast: Forecast){
                                toast(forecast.date)
                            }
                        })*/
            }
        }

        //val f1 = Forecast(Date(), 27.5f, "Shiny day")
        //val (date, temperature, details) = f1
        /**
         * val date = f1.component1()
         * val temperature = f1.component2()
         * val details = f1.component3()
         */

    }
}
