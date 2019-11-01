package com.aldi.retrox.Model

import com.google.gson.annotations.SerializedName

data class Joke(
    @SerializedName("id") val jokeId: Int,
    @SerializedName("joke") val jokeText: String
)