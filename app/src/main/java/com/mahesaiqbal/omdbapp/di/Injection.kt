package com.mahesaiqbal.omdbapp.di

import android.app.Application
import com.mahesaiqbal.omdbapp.data.source.OMDBRepository
import com.mahesaiqbal.omdbapp.data.source.remote.RemoteRepository

class Injection {

    companion object {
        fun provideRepository(): OMDBRepository {
            val remoteRepository = RemoteRepository.getInstance()

            return OMDBRepository.getInstance(remoteRepository!!)!!
        }
    }
}