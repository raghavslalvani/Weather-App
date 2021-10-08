package com.eurofins.weatherapp.weather

data class Sys(
    val type: Int = 0, val id: Long = 1,
    val country: String, val sunrise: Long, val sunset: Float,
)