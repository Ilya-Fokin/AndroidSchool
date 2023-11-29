package com.example.lesson_10_fokin.presentation

import com.example.lesson_10_fokin.data.model.Bridge

interface BridgeLoadCallback {
    fun onSuccess(bridges: List<Bridge>)
    fun onError(error: Throwable)
}