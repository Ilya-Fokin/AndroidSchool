package com.example.lesson_12_fokin.data.repository

import com.example.lesson_12_fokin.data.remote.MainApiService
import com.example.lesson_12_fokin.data.remote.model.ApiBridge
import com.example.lesson_12_fokin.data.remote.model.BridgeMapper
import com.example.lesson_12_fokin.di.applicationComponent.modules.DispatcherProvider
import com.example.lesson_12_fokin.data.remote.model.Bridge
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BridgesRepositoryImpl @Inject constructor(
    private val apiService: MainApiService,
    private val dispatcherProvider: DispatcherProvider
) : BridgesRepository {

    override suspend fun getBridges(): List<Bridge> = withContext(dispatcherProvider.io) {
        return@withContext apiService.getBridges().map { BridgeMapper.toBridge(it) }
    }

    override suspend fun getBridgeById(id: Int): ApiBridge = withContext(dispatcherProvider.io) {
        return@withContext apiService.getBridgeById(id)
    }
}