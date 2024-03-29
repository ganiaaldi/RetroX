package com.aldi.retrox.Interface

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object JokeApi {
    fun createService(): JokeApiService = Retrofit.Builder()
        .baseUrl("http://api.icndb.com")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //change to RX java 2
        .build()
        .create(JokeApiService::class.java)
}