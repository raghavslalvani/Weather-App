package com.eurofins.weatherapp.weather

data class MainWeatherFeatures(
    val temp: Float, val feels_like: Float,
    val temp_min: Float,
    val temp_max: Float,
    val pressure: Float,
    val humidity: Float,
    val sea_level: Float,
    val grnd_level: Float,
)