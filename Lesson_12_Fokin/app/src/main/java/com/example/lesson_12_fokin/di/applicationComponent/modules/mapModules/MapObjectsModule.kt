package com.example.lesson_12_fokin.di.applicationComponent.modules.mapModules

import com.example.lesson_12_fokin.di.scope.MapScope
import com.example.lesson_12_fokin.presentation.bridges.Bridge
import com.yandex.mapkit.map.PlacemarkMapObject
import dagger.Module
import dagger.Provides

@Module
class MapObjectsModule {

    @MapScope
    @Provides
    fun provideMapObjects(): MutableMap<PlacemarkMapObject, Bridge> {
        return mutableMapOf()
    }
}