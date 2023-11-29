package com.example.lesson_10_fokin.presentation

import android.app.Application
import com.example.lesson_10_fokin.R
import com.yandex.mapkit.MapKitFactory

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(getString(R.string.yandex_map_api_key))
        MapKitFactory.initialize(this)
    }
}