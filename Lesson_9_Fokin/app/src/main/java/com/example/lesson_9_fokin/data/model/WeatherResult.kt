package com.example.lesson_9_fokin.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class WeatherResult(
    @SerializedName("weather") val weather: List<Weather>?,
    @SerializedName("main") val main: Main?,
    @SerializedName("wind") val wind: Wind?,
    @SerializedName("clouds") val clouds: Clouds?,
    @SerializedName("name") val name: String?,
) : Parcelable

@Parcelize
class Weather(
    @SerializedName("main") val main: String?,
) : Parcelable

@Parcelize
class Main(
    @SerializedName("temp") val temp: String?,
    @SerializedName("temp_min") val tempMin: String?,
    @SerializedName("temp_max") val tempMax: String?,
) : Parcelable

@Parcelize
class Wind(
    @SerializedName("speed") val speed: Double?,
) : Parcelable

@Parcelize
class Clouds(
    @SerializedName("all") val all: String?,
) : Parcelable