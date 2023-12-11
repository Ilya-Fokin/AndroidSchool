package com.example.lesson_12_fokin.data.repository

import com.example.lesson_12_fokin.data.remote.model.ApiBridge
import com.example.lesson_12_fokin.presentation.bridges.Bridge

interface BridgesRepository {

    /** Получаем список всех мостов из API */
    suspend fun getBridges(): List<Bridge>

    /** Получаем мост по его id из API */
    suspend fun getBridgeById(id: Int): ApiBridge
}