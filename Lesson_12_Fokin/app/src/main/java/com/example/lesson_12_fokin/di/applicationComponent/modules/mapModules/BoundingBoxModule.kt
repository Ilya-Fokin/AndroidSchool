package com.example.lesson_12_fokin.di.applicationComponent.modules.mapModules

import com.example.lesson_12_fokin.di.scope.MapScope
import com.example.lesson_12_fokin.presentation.BoundingBoxBuilder
import dagger.Module
import dagger.Provides

@Module
class BoundingBoxModule {

    @MapScope
    @Provides
    fun provideBoundingBoxBuilder(): BoundingBoxBuilder {
        return BoundingBoxBuilder()
    }
}