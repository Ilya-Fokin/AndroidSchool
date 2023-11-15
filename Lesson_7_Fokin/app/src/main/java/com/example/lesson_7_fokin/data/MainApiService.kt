package com.example.lesson_7_fokin.data

import com.example.lesson_7_fokin.data.model.Bridge
import retrofit2.http.GET

interface MainApiService {
    @GET("bridges")
    suspend fun getBridges(): List<Bridge>
}