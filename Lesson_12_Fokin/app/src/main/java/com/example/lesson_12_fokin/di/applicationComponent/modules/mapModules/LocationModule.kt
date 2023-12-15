package com.example.lesson_12_fokin.di.applicationComponent.modules.mapModules

import android.location.Location
import com.example.lesson_12_fokin.BridgesApplication
import com.example.lesson_12_fokin.di.scope.MapScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides

private const val INTERVAL_MILLIS: Long = 100

@Module
class LocationModule {

    @MapScope
    @Provides
    fun provideLocation(): Location {
        return Location("last_location")
    }

    @MapScope
    @Provides
    fun provideLocationRequest(): LocationRequest {
        return LocationRequest.Builder(INTERVAL_MILLIS).build()
    }

    @MapScope
    @Provides
    fun provideFusedLocationProviderClient(application: BridgesApplication): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(application.applicationContext)
    }

}