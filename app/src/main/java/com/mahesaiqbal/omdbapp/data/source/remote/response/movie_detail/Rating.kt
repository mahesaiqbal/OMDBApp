package com.mahesaiqbal.omdbapp.data.source.remote.response.movie_detail


import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("Source")
    val source: String,
    @SerializedName("Value")
    val value: String
)