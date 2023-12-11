package com.example.lesson_12_fokin.di.applicationComponent

import com.example.lesson_12_fokin.BridgesApplication
import com.example.lesson_12_fokin.di.applicationComponent.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<BridgesApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: BridgesApplication): ApplicationComponent
    }
}