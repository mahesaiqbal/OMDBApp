package com.mahesaiqbal.omdbapp.network

import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_detail.MovieDetailResponse
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("/")
    fun getMovieSearchList(
        @Query("apikey") apiKey: String,
        @Query("s") querySearch: String,
        @Query("page") page: Int
    ): Observable<MovieResponse>

    @GET("/")
    fun getMovieDetail(
        @Query("apikey") apiKey: String,
        @Query("i") imdbId: String
    ): Observable<MovieDetailResponse>
}