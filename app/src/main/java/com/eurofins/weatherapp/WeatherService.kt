package com.eurofins.weatherapp

import android.annotation.SuppressLint
import com.eurofins.weatherapp.weather.WeatherInfo
import com.eurofins.weatherapp.weatherprediction.WeatherForecastinfo
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

const val baseValue = "https://api.openweathermap.org/data/2.5/"
const val apiKey = "db56c752cfc4cd7455810275b9736ffc"

interface WeatherInterface {

    @GET("weather?appid=$apiKey")
    fun getWeather(@Query("zip") code_country: String): Call<WeatherInfo>

    @GET("weather?appid=$apiKey")
    fun getWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Call<WeatherInfo>

    @GET("onecall?exclude=minutely,hourly,alerts&appid=$apiKey")
    fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
    ): Call<WeatherForecastinfo>
}

object WeatherService {
    val weatherInstance: WeatherInterface

    init {
        val retrofit =
            Retrofit.Builder().baseUrl(baseValue).addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient()).build()
        weatherInstance = retrofit.create(WeatherInterface::class.java)
    }

    @SuppressLint("TrustAllX509TrustManager")
    private fun createOkHttpClient(): OkHttpClient? {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(
                @SuppressLint("CustomX509TrustManager")
                object : X509TrustManager {
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?,
                    ) {
                    }

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?,
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOf()
                    }
                }
            )
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            OkHttpClient.Builder()
                .sslSocketFactory(sslContext.socketFactory)
                .hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}