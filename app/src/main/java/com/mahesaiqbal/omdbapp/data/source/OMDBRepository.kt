package com.mahesaiqbal.omdbapp.data.source

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.mahesaiqbal.omdbapp.data.source.remote.RemoteRepository
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_detail.MovieDetailResponse
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.Search

class OMDBRepository(var remoteRepository: RemoteRepository) : OMDBDataSource {

    companion object {
        @Volatile
        private var INSTANCE: OMDBRepository? = null

        fun getInstance(remoteData: RemoteRepository): OMDBRepository? {
            if (INSTANCE == null) {
                synchronized(OMDBRepository::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = OMDBRepository(remoteData)
                    }
                }
            }
            return INSTANCE
        }
    }

    val gson = Gson()

    override fun getMovieSearchList(querySearch: String, page: Int): MutableLiveData<ArrayList<Search>> {
        val movieSearchResults: MutableLiveData<ArrayList<Search>> = MutableLiveData()

        remoteRepository.getMovieSearchList(querySearch, page, object : RemoteRepository.LoadMovieSearchCallback {
            override fun onMoviesReceived(movies: ArrayList<Search>) {
                movieSearchResults.postValue(movies)
            }
        })

        return movieSearchResults
    }

    override fun getMovieDetail(imdbId: String): MutableLiveData<MovieDetailResponse> {
        val movieDetailResults: MutableLiveData<MovieDetailResponse> = MutableLiveData()

        remoteRepository.getMovieDetail(imdbId, object : RemoteRepository.LoadMovieDetailCallback {
            override fun onMovieDetailReceived(movieDetail: MovieDetailResponse) {
                movieDetailResults.postValue(movieDetail)
            }
        })

        return movieDetailResults
    }
}