package com.example.lesson_12_fokin.di.applicationComponent.modules

import com.example.lesson_12_fokin.data.remote.MainApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "http://gdemost.handh.ru:1235/"

/** Модуль для ретрофита */
@Module
class ApiServiceModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideMainApiService(
        retrofit: Retrofit,
    ): MainApiService {
        return retrofit.create(MainApiService::class.java)
    }
}