package com.mahesaiqbal.omdbapp.data.source.remote

import com.google.gson.Gson
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_detail.MovieDetailResponse
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.MovieResponse
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.Search
import com.mahesaiqbal.omdbapp.network.Client
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RemoteRepository {
    companion object {
        private var INSTANCE: RemoteRepository? = null

        fun getInstance(): RemoteRepository? {
            if (INSTANCE == null) {
                INSTANCE = RemoteRepository()
            }
            return INSTANCE
        }
    }

    private val apiService = Client.create()
    private val compositeDisposable = CompositeDisposable()
    private val gson = Gson()

    private val apiKey = "4f884fce"

    fun getMovieSearchList(querySearch: String, page: Int, callback: LoadMovieSearchCallback) {
//        EspressoIdlingResource.increment()

        apiService.getMovieSearchList(apiKey, querySearch, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MovieResponse> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: MovieResponse) {
                    callback.onMoviesReceived(t.search as ArrayList<Search>)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onComplete() {
//                    if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow) {
//                        EspressoIdlingResource.decrement()
//                    }
                }
            })
    }

    fun getMovieDetail(imdbId: String, callback: LoadMovieDetailCallback) {
//        EspressoIdlingResource.increment()

        apiService.getMovieDetail(apiKey, imdbId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<MovieDetailResponse> {
                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: MovieDetailResponse) {
                    callback.onMovieDetailReceived(t)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onComplete() {
//                    if (!EspressoIdlingResource.getEspressoIdlingResource().isIdleNow) {
//                        EspressoIdlingResource.decrement()
//                    }
                }
            })
    }

    fun clearDisposable() {
        compositeDisposable.clear()
    }

    interface LoadMovieSearchCallback {
        fun onMoviesReceived(movies: ArrayList<Search>)
    }

    interface LoadMovieDetailCallback {
        fun onMovieDetailReceived(movieDetail: MovieDetailResponse)
    }
}