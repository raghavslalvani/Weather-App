package com.eurofins.weatherapp.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eurofins.weatherapp.WeatherService
import com.eurofins.weatherapp.weather.WeatherInfo
import com.eurofins.weatherapp.weatherprediction.WeatherForecastinfo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private var _temperatureAndPlace = MutableLiveData<String>()
    val temperatureAndPlace: LiveData<String> get() = _temperatureAndPlace

    private var _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private var _cloudCover = MutableLiveData<String>()
    val cloudCover: LiveData<String> get() = _cloudCover

    private var _pressure = MutableLiveData<String>()
    val pressure: LiveData<String> get() = _pressure

    private var _dataset: MutableList<DailyForecastList> =
        mutableListOf(DailyForecastList(1633327200, 24.0f,
            "rainy"))
    val dataset get() = _dataset

    fun getTemperature(text: String) {
        getWeather(text)
    }

    fun getPlace(lat: Double, lon: Double) {
        getCurrentPlace(lat, lon)
    }

    fun getWeatherForecast(lat: Double, lon: Double) {
        _dataset.clear()
        getWeatherForecastDaily(lat, lon)
    }

    private fun getCurrentPlace(lat: Double, lon: Double) {
        val weatherForecast = WeatherService.weatherInstance.getWeather(lat, lon)
        weatherForecast.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(
                call: Call<WeatherInfo>,
                response: Response<WeatherInfo>,
            ) {
                val result = response.body()
                if (result != null) {
                    _temperatureAndPlace.value = " Place: " + result.name +
                            "\nTemp = " + (result.main.temp - 273) + " C"
                    _description.value = "Description\n " + result.weather[0].main
                    _cloudCover.value = "Cloud Cover\n" + result.clouds.all.toString() + "%"
                    _pressure.value = "Pressure\n " + result.main.pressure.toString() + " mbar"
                } else {
                    _temperatureAndPlace.value = "Incorrect Lat or lon"
                }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                _temperatureAndPlace.value = "Error in fetching please check your connection"
            }
        })

    }

    private fun getWeatherForecastDaily(lat: Double, lon: Double) {
        val weatherForecast = WeatherService.weatherInstance.getWeatherForecast(lat, lon)
        weatherForecast.enqueue(object : Callback<WeatherForecastinfo> {
            @SuppressLint("NewApi")
            override fun onResponse(
                call: Call<WeatherForecastinfo>,
                response: Response<WeatherForecastinfo>,
            ) {
                val result = response.body()

                if (result != null) {
                    for (item in result.daily) {
                        _dataset.add(DailyForecastList(item.dt,
                            (item.temp.day - 273),
                            item.weather[0].description))
                    }
                } else {
                    _temperatureAndPlace.value = "Incorrect Lat or lon"
                }
            }

            override fun onFailure(call: Call<WeatherForecastinfo>, t: Throwable) {
                _temperatureAndPlace.value = "Error in fetching please check your connection"
            }
        })
    }

    private fun getWeather(pin: String) {
        val pinCountry = "$pin,in"
        Log.d("Wagle", " your pincode  $pinCountry")
        val weather = WeatherService.weatherInstance.getWeather(code_country = pinCountry)
        weather.enqueue(object : Callback<WeatherInfo> {
            override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                val result = response.body()
                Log.d("Wagle", " you are inside response")
                if (result != null) {
                    val temp = result.main.temp - 273
                    _temperatureAndPlace.value = " Place: " + result.name +
                            "\nTemp = " + temp.toString() + " C"
                    _description.value = "Description\n " + result.weather[0].main
                    _cloudCover.value = "Cloud Cover\n" + result.clouds.all.toString() + "%"
                    _pressure.value = "Pressure\n " + result.main.pressure.toString() + " mbar"

                    Log.d("Wagle", "Retrofit response is successfully fetched")
                    Log.d("Wagle", "your  response text ${temperatureAndPlace.value}")
                } else {
                    Log.d("Wagle", "Error in pincode")
                    _temperatureAndPlace.value = "Incorrect Pin"
                }
            }

            override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                Log.d("Wagle", "Could not fetch the retrofit response $t.message() ")
                _temperatureAndPlace.value = "Error in fetching please check your connection"
            }
        })
        Log.d("Wagle", "your text $temperatureAndPlace")
    }
}