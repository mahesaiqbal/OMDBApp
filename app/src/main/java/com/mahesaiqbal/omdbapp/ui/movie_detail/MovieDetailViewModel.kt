package com.mahesaiqbal.omdbapp.ui.movie_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mahesaiqbal.omdbapp.data.source.OMDBRepository
import com.mahesaiqbal.omdbapp.data.source.remote.RemoteRepository
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_detail.MovieDetailResponse

class MovieDetailViewModel(var omdbRepository: OMDBRepository) : ViewModel() {
    private val remoteRepository = RemoteRepository()



    fun getMovieDetail(imdbId: String): MutableLiveData<MovieDetailResponse> =
        omdbRepository.getMovieDetail(imdbId)

    override fun onCleared() {
        super.onCleared()
        remoteRepository.clearDisposable()
    }
}