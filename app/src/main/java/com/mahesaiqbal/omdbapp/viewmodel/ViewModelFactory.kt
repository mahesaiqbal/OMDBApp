package com.mahesaiqbal.omdbapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.mahesaiqbal.omdbapp.data.source.OMDBRepository
import com.mahesaiqbal.omdbapp.di.Injection
import com.mahesaiqbal.omdbapp.ui.main.MainViewModel
import com.mahesaiqbal.omdbapp.ui.movie_detail.MovieDetailViewModel

class ViewModelFactory(omdbRepository: OMDBRepository) : NewInstanceFactory() {

    var mOMDBRepository: OMDBRepository = omdbRepository

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory? {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = ViewModelFactory(Injection.provideRepository())
                    }
                }
            }
            return INSTANCE
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(mOMDBRepository) as T
        } else if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(mOMDBRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}