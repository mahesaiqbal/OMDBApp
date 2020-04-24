package com.mahesaiqbal.omdbapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.mahesaiqbal.omdbapp.R
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.Search
import com.mahesaiqbal.omdbapp.ui.main.MainAdapter.MovieViewHolder
import kotlinx.android.synthetic.main.item_movie.view.*

class MainAdapter(var movies: ArrayList<Search>, var callback: MovieCallback) : Adapter<MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder
            = MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindItem(movies[position], callback)
    }

    override fun getItemCount(): Int = movies.size

    fun refreshAdapter(moviesResult: ArrayList<Search>) {
        movies.addAll(moviesResult)
        notifyItemRangeChanged(0, movies.size)
    }

    inner class MovieViewHolder(itemView: View) : ViewHolder(itemView) {

        fun bindItem(movie: Search, callback: MovieCallback) {
            itemView.tv_title.text = movie.title
            itemView.tv_release_year.text = movie.year

            Glide.with(itemView.context)
                .load(movie.poster)
                .into(itemView.iv_poster)

            itemView.tv_detail.setOnClickListener { callback.onMovieClicked(movie) }
        }
    }

    interface MovieCallback {
        fun onMovieClicked(movie: Search)
    }
}