package com.eurofins.weatherapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eurofins.weatherapp.R
import com.eurofins.weatherapp.adapter.WeatherForecastAdapter.WeatherForecastViewHolder
import com.eurofins.weatherapp.data.DailyForecastList

class WeatherForecastAdapter(private val dataset: List<DailyForecastList>) :
    RecyclerView.Adapter<WeatherForecastViewHolder>() {
    class WeatherForecastViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView1: TextView = view.findViewById(R.id.temperature)
        val textView2: TextView = view.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        Log.d("Wagle", "You are inside onCreateViewHolder")
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_list, parent, false)
        return WeatherForecastViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        Log.d("Wagle", "You are inside onBindViewHolder")
        val item = dataset[position]
        val desc = item.description
        val temperature = item.temp.toString()
        holder.textView1.text = temperature
        holder.textView2.text = desc
    }

    override fun getItemCount(): Int {
        Log.d("Wagle", "You are inside getItemCount")
        return dataset.size
    }
}


