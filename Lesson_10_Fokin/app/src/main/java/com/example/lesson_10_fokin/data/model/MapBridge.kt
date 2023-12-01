package com.example.lesson_10_fokin.data.model

import com.google.gson.annotations.SerializedName
import com.yandex.mapkit.geometry.Point

class MapBridge(
    val id: Int?,
    val name: String?,
    val nameEng: String?,
    val description: String?,
    val descriptionEng: String?,
    val divorces: List<Divorce>?,
    val closedUrl: String?,
    val openUrl: String?,
    val public: Boolean?,
    val point: Point,
    val iconId: Int,
)