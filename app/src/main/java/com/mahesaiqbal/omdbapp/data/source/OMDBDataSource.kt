package com.mahesaiqbal.omdbapp.data.source

import androidx.lifecycle.MutableLiveData
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_detail.MovieDetailResponse
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.Search

interface OMDBDataSource {
    fun getMovieSearchList(querySearch: String, page: Int): MutableLiveData<ArrayList<Search>>

    fun getMovieDetail(imdbId: String): MutableLiveData<MovieDetailResponse>
}