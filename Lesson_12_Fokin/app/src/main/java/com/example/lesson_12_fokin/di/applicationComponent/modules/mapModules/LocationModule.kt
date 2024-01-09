package com.example.lesson_12_fokin.di.applicationComponent.modules.mapModules

import com.example.lesson_12_fokin.BridgesApplication
import com.example.lesson_12_fokin.di.scope.MapScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

@Module
class LocationModule {

    @MapScope
    @Provides
    fun provideFusedLocationProviderClient(application: BridgesApplication): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application.applicationContext)
    }
}