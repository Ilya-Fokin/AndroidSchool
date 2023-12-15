package com.example.lesson_12_fokin.di.applicationComponent.modules

import androidx.lifecycle.ViewModel
import com.example.lesson_12_fokin.di.applicationComponent.ViewModelKey
import com.example.lesson_12_fokin.presentation.bridges.viewModels.BridgesViewModel
import com.example.lesson_12_fokin.presentation.bridges.viewModels.DetailBridgeViewModel
import com.example.lesson_12_fokin.presentation.bridges.viewModels.MapBridgesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/** Модуль для вью моделей */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BridgesViewModel::class)
    abstract fun bridgesViewModel(viewModel: BridgesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailBridgeViewModel::class)
    abstract fun detailBridgesViewModel(viewModel: DetailBridgeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MapBridgesViewModel::class)
    abstract fun mapBridgesViewModel(viewModel: MapBridgesViewModel): ViewModel
}