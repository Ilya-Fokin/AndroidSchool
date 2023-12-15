package com.example.lesson_12_fokin.di.applicationComponent.modules

import dagger.Binds
import dagger.Module
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Module
abstract class CoroutineModule {
    @Binds
    abstract fun provideDispatcherProvider(
        dispatcherProvider: DispatcherProviderImpl,
    ): DispatcherProvider
}

class DispatcherProviderImpl @Inject constructor() : DispatcherProvider {
    override val io = Dispatchers.IO
    override val main = Dispatchers.Main
    override val default = Dispatchers.Default
}

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher
}