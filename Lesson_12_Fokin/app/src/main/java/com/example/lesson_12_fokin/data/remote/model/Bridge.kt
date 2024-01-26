package com.example.lesson_12_fokin.data.remote.model

import com.example.lesson_12_fokin.presentation.bridges.StatusBridge
import com.yandex.mapkit.geometry.Point

class Bridge(
    val id: Int,
    val name: String,
    val nameEng: String,
    val description: String,
    val descriptionEng: String,
    val divorces: List<ApiDivorce>,
    val status: StatusBridge,
    val currentImgUrl: String,
    val public: Boolean,
    val point: Point,
    val iconId: Int,
)