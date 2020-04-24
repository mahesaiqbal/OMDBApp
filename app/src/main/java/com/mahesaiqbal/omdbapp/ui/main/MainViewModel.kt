package com.mahesaiqbal.omdbapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mahesaiqbal.omdbapp.data.source.OMDBRepository
import com.mahesaiqbal.omdbapp.data.source.remote.RemoteRepository
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.Search

class MainViewModel(var omdbRepository: OMDBRepository) : ViewModel() {
    private val remoteRepository = RemoteRepository()

    fun getMovieSearchList(querySearch: String, page: Int): MutableLiveData<ArrayList<Search>> =
        omdbRepository.getMovieSearchList(querySearch, page)

    override fun onCleared() {
        super.onCleared()
        remoteRepository.clearDisposable()
    }
}