package com.example.lesson_12_fokin.di.applicationComponent.modules

import com.example.lesson_12_fokin.data.repository.BridgesRepository
import com.example.lesson_12_fokin.data.repository.BridgesRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun provideRepository(
        bridgesRepository: BridgesRepositoryImpl
    ): BridgesRepository {
        return bridgesRepository
    }
}