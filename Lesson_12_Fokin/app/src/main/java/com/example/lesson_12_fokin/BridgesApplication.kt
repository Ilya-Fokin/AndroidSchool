package com.example.lesson_12_fokin

import com.example.lesson_12_fokin.di.applicationComponent.DaggerApplicationComponent
import com.yandex.mapkit.MapKitFactory
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class BridgesApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(getString(R.string.yandex_map_api_key))
        MapKitFactory.initialize(this)
    }
}
