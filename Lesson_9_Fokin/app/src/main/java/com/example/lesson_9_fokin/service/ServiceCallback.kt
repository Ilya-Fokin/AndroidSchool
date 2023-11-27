package com.example.lesson_9_fokin.service

import com.example.lesson_9_fokin.data.model.Weather
import com.example.lesson_9_fokin.data.model.WeatherResult

interface ServiceCallback {
    fun bindWeather(weather: WeatherResult)
    fun sendError(error: String)
    fun showProgressBar(isVisible: Boolean)
}