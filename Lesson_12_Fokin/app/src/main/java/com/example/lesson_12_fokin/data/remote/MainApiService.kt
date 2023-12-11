package com.example.lesson_12_fokin.data.remote

import com.example.lesson_12_fokin.data.remote.model.ApiBridge
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApiService {

    @GET("bridges")
    suspend fun getBridges(): List<ApiBridge>

    @GET("bridges/{id}")
    suspend fun getBridgeById(@Path("id") id: Int): ApiBridge
}