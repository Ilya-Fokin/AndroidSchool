package com.example.lesson_9_fokin.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.view.isVisible
import com.example.lesson_9_fokin.data.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadWeatherService : Service() {

    var serviceCallback: ServiceCallback? = null

    override fun onBind(intent: Intent?): IBinder {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                try {
                    val weatherResult = ApiClient.apiService.getWeather(QUERY, APP_ID)
                    serviceCallback?.bindWeather(weatherResult)
                    serviceCallback?.showProgressBar(false)
                } catch (ex: Exception) {
                    serviceCallback?.sendError(ex.message.toString())
                }
                delay(60000)
            }
        }
        return MyBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        serviceCallback = null
        return super.onUnbind(intent)
    }

    inner class MyBinder : Binder() {
        val service: LoadWeatherService
            get() = this@LoadWeatherService
    }

    companion object {
        private const val APP_ID = "a924f0f5b30839d1ecb95f0a6416a0c2"

        private const val QUERY = "saratov"
    }
}