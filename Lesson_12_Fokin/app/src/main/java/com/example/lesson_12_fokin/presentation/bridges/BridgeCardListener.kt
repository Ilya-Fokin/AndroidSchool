package com.example.lesson_12_fokin.presentation.bridges

import com.example.lesson_12_fokin.data.remote.model.Bridge

fun interface BridgeCardListener {
    fun setOnClickCard(item: Bridge)
}