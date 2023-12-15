package com.example.lesson_12_fokin.presentation.bridges

import com.example.lesson_12_fokin.data.remote.model.Divorce
import com.yandex.mapkit.geometry.Point

class Bridge(
    val id: Int,
    val name: String,
    val nameEng: String,
    val description: String,
    val descriptionEng: String,
    val divorces: List<Divorce>,
    val status: StatusBridge,
    val currentImgUrl: String,
    val public: Boolean,
    val point: Point,
    val iconId: Int,
)