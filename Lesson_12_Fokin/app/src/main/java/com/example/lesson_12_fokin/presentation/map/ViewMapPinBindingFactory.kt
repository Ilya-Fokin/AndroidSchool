package com.example.lesson_12_fokin.presentation.map

import android.view.LayoutInflater
import com.example.lesson_12_fokin.databinding.ViewMapPinBinding
import javax.inject.Inject

class ViewMapPinBindingFactory @Inject constructor() {

    fun create(layoutInflater: LayoutInflater): ViewMapPinBinding {
        return ViewMapPinBinding.inflate(layoutInflater)
    }
}