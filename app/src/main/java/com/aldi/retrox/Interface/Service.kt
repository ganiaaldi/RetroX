package com.aldi.retrox.Interface

import com.aldi.retrox.Model.Github
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable

interface Service {
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Observable<Github>
}