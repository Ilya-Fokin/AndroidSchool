package com.example.lesson_12_fokin.di.applicationComponent.modules

import com.example.lesson_12_fokin.di.scope.MapScope
import com.example.lesson_12_fokin.di.applicationComponent.modules.mapModules.MapModule
import com.example.lesson_12_fokin.presentation.bridges.fragments.BridgesFragment
import com.example.lesson_12_fokin.presentation.bridges.fragments.DetailBridgeFragment
import com.example.lesson_12_fokin.presentation.bridges.fragments.MapBridgesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/** Модуль фрагментов */
@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bridgesFragment(): BridgesFragment

    @ContributesAndroidInjector
    abstract fun detailBridgeFragment(): DetailBridgeFragment

    /** Подключаем все модули, относящиеся к Map к данному subcomponent */
    @MapScope
    @ContributesAndroidInjector(
        modules = [
            MapModule::class
        ]
    )
    abstract fun mapBridgeFragment(): MapBridgesFragment

}