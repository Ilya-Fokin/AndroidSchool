package com.example.lesson_10_fokin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
class Bridge(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("name_eng") val nameEng: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("description_eng") val descriptionEng: String?,
    @SerializedName("divorces") val divorces: List<Divorce>?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?,
    @SerializedName("photo_close_url") val closedUrl: String?,
    @SerializedName("photo_open_url") val openUrl: String?,
    @SerializedName("public") val public: Boolean?,
)
class Divorce(
    @SerializedName("start") val start: String?,
    @SerializedName("end") val end: String?
)
