package com.obi.weather.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.obi.weather.R
import com.obi.weather.domain.model.Forecast
import com.obi.weather.domain.model.ForecastList
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import com.obi.weather.ui.utils.ctx
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_forecast.*

class ForecastListAdapter(private val weekForecast: ForecastList,
                          private val itemClick: (Forecast) -> Unit) :
        RecyclerView.Adapter<ForecastListAdapter.ViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
            val view = LayoutInflater.from(parent.ctx)
                    .inflate(R.layout.item_forecast, parent, false)

            return ViewHolder(view, itemClick)
        }

        override fun onBindViewHolder(holder: ViewHolder,
                                      position: Int){
             holder.bindForecast(weekForecast[position])
        }

        override fun getItemCount(): Int = weekForecast.size

        class ViewHolder(override val containerView: View,
                         private val itemClick: (Forecast) -> Unit) :
                RecyclerView.ViewHolder(containerView), LayoutContainer {

                fun bindForecast(forecast: Forecast){
                        with(forecast){
                            Picasso.with(itemView.ctx).load(iconUrl).into(icon)
                            dateText.text = date
                            descriptionText.text = description
                            maxTemperature.text = "${high}º"
                            minTemperature.text = "${low}º"
                            itemView.setOnClickListener{ itemClick(this)}
                        }
                }

        }

}
