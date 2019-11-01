package com.aldi.retrox.Interface

import com.aldi.retrox.Model.JokeApiResponse
import io.reactivex.Single
import retrofit2.http.GET

interface JokeApiService {
    @GET("jokes/random")
    fun randomJoke(): Single<JokeApiResponse>

}