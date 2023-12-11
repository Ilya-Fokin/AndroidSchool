package com.example.lesson_12_fokin.di.applicationComponent.modules.mapModules

import com.example.lesson_12_fokin.di.scope.MapScope
import com.example.lesson_12_fokin.presentation.map.ViewMapPinBindingFactory
import dagger.Module
import dagger.Provides

@Module
class MapPinViewModule {

    @MapScope
    @Provides
    fun provideViewMapPinBindingFactory(): ViewMapPinBindingFactory {
        return ViewMapPinBindingFactory()
    }
}