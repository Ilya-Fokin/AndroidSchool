package com.example.lesson_12_fokin.di.applicationComponent.modules.mapModules

import dagger.Module
import dagger.android.support.AndroidSupportInjectionModule

@Module(
    includes = [
        LocationModule::class,
        AndroidSupportInjectionModule::class,
    ]
)
class MapModule