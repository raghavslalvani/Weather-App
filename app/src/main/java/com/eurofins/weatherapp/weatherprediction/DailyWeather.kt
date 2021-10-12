package com.eurofins.weatherapp.weatherprediction

import com.eurofins.weatherapp.weather.Weather


data class DailyWeather(
    val dt: Long, val sunrise: Long, val sunset: Long, val moonrise: Long,
    val moonset: Long, val moon_phase: Float, val temp: Temp, val feels_like: FeelsLike,
    val pressure: Long, val humidity: Int, val dew_point: Float,
    val wind_speed: Float, val wind_deg: Float,
    val wind_gust: Float, val weather: List<Weather>, val clouds: Int,
    val pop: Float, val rain: Float, val uvi: Float,
)
