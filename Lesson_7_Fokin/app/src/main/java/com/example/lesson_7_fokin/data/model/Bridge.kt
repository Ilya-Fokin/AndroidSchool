package com.example.lesson_7_fokin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import retrofit2.http.Url
import java.util.Date

@Parcelize
class Bridge(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("name_eng") val nameEng: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("description_eng") val descriptionEng: String?,
    @SerializedName("divorces") val divorces: List<Divorce>?,
    @SerializedName("lat") val lat: String?,
    @SerializedName("lng") val lng: String?,
    @SerializedName("photo_close_url") val closedUrl: String?,
    @SerializedName("photo_open_url") val openUrl: String?,
    @SerializedName("public") val public: Boolean?,
    var status: StatusBridge = StatusBridge.DIVORCED
) : Parcelable

@Parcelize
class Divorce(
    @SerializedName("start") val start: String?,
    @SerializedName("end") val end: String?
) : Parcelable

