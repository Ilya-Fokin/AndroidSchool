package com.example.lesson_9_fokin.data

import com.example.lesson_9_fokin.data.model.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("data/2.5/weather?units=metric")
    suspend fun getWeather(@Query("q") q: String, @Query("appid") appId: String): WeatherResult
}