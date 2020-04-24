package com.mahesaiqbal.omdbapp.ui.movie_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.mahesaiqbal.omdbapp.R
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_detail.MovieDetailResponse
import com.mahesaiqbal.omdbapp.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    lateinit var viewModel: MovieDetailViewModel
    lateinit var imdbId: String

    companion object {
        fun obtainViewModel(activity: AppCompatActivity): MovieDetailViewModel {
            val factory = ViewModelFactory.getInstance()
            return ViewModelProviders.of(activity, factory).get(MovieDetailViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        supportActionBar?.apply {
            title = "Detail Film"
            setDisplayHomeAsUpEnabled(true)
        }

        viewModel = obtainViewModel(this)

        val extras: Bundle? = intent.extras
        if (extras != null) {
            val imdbIdFromIntent = extras.getString("imdb_id")
            if (imdbIdFromIntent != null) {
                imdbId = imdbIdFromIntent
            }
        }

        viewModel.getMovieDetail(imdbId).observe(this, getMovieDetail)
    }

    private val getMovieDetail = Observer<MovieDetailResponse> { movieDetail ->
        if (movieDetail != null) {
            populateMovie(movieDetail)
        }
    }

    private fun populateMovie(movieDetail: MovieDetailResponse) {
        Glide.with(this)
            .load(movieDetail.poster)
            .into(iv_poster)

        tv_title.text = movieDetail.title
        tv_genre.text = movieDetail.genre
        tv_runtime.text = movieDetail.runtime
        tv_release_year.text = movieDetail.year
        tv_rate.text = movieDetail.imdbRating
        tv_plot.text = movieDetail.plot
    }
}
