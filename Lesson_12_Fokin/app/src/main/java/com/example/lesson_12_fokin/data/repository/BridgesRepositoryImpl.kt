package com.example.lesson_12_fokin.data.repository

import com.example.lesson_12_fokin.data.remote.MainApiService
import com.example.lesson_12_fokin.data.remote.model.ApiBridge
import com.example.lesson_12_fokin.data.remote.model.BridgeMapper
import com.example.lesson_12_fokin.presentation.bridges.Bridge
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BridgesRepositoryImpl @Inject constructor(
    private val apiService: MainApiService
) : BridgesRepository {

    override suspend fun getBridges(): List<Bridge> = withContext(Dispatchers.IO) {
        return@withContext apiService.getBridges().map { BridgeMapper.toBridge(it) }
    }

    override suspend fun getBridgeById(id: Int): ApiBridge = withContext(Dispatchers.IO) {
        return@withContext apiService.getBridgeById(id)
    }
}