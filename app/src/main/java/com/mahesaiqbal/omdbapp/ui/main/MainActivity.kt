package com.mahesaiqbal.omdbapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahesaiqbal.omdbapp.R
import com.mahesaiqbal.omdbapp.data.source.remote.response.movie_search.Search
import com.mahesaiqbal.omdbapp.viewmodel.ViewModelFactory
import com.mahesaiqbal.omdbapp.ui.main.MainAdapter.MovieCallback
import com.mahesaiqbal.omdbapp.ui.movie_detail.MovieDetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), MovieCallback {

    private val TAG = javaClass.simpleName

    lateinit var movieAdapter: MainAdapter
    lateinit var viewModel: MainViewModel

    lateinit var searchView: SearchView

    private var isLoading by Delegates.notNull<Boolean>()
    private var page by Delegates.notNull<Int>()
    private var totalPage by Delegates.notNull<Int>()

    // Default value (Avengers)
    private var querySearch = "Avengers"

    companion object {
        fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
            val factory = ViewModelFactory.getInstance()
            return ViewModelProviders.of(activity, factory).get(MainViewModel::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.apply {
            title = "Daftar Film"
        }

        viewModel = obtainViewModel(this)

        page = 1
        totalPage = 40

        loadData(querySearch, page)
        initListener()
    }

    private fun loadData(query: String, page: Int) {
        showLoading(true)
        viewModel.getMovieSearchList(query, page).observe(this, getMovieSearchList)
    }

    private val getMovieSearchList = Observer<ArrayList<Search>> { movies ->
        if (movies != null) {
            if (page == 1) {
                movieAdapter = MainAdapter(movies, this)

                rv_movies.apply {
                    layoutManager = LinearLayoutManager(context)
                    setHasFixedSize(true)
                    adapter = movieAdapter
                }

                movieAdapter.notifyDataSetChanged()
            } else {
                movieAdapter.refreshAdapter(movies)
            }
        }

        hideLoading()
    }

    private fun showLoading(isRefresh: Boolean) {
        isLoading = true
        pb_loading.visibility = View.VISIBLE
        rv_movies.visibility.let {
            if (isRefresh) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun hideLoading() {
        isLoading = false
        pb_loading.visibility = View.GONE
        rv_movies.visibility = View.VISIBLE
    }

    private fun initListener() {
        rv_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val countItem = linearLayoutManager.itemCount
                val lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                Log.d(TAG, "countItem: $countItem")
                Log.d(TAG, "lastVisiblePosition: $lastVisiblePosition")
                Log.d(TAG, "isLastPosition: $isLastPosition")

                if (!isLoading && isLastPosition && page < totalPage) {
                    showLoading(true)
                    page = page.plus(1)
                    loadData(querySearch, page)
                }
            }
        })
    }

    override fun onMovieClicked(movie: Search) {
        val intent = Intent(this, MovieDetailActivity::class.java)
        intent.putExtra("imdb_id", movie.imdbID)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        if (searchItem != null) {
            searchView = MenuItemCompat.getActionView(searchItem) as SearchView
            searchView.setOnCloseListener(object : SearchView.OnCloseListener {
                override fun onClose(): Boolean {
                    return true
                }
            })

            val searchPlate =
                searchView.findViewById(androidx.appcompat.R.id.search_src_text) as EditText
            searchPlate.hint = "Search"
            val searchPlateView: View =
                searchView.findViewById(androidx.appcompat.R.id.search_plate)
            searchPlateView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    android.R.color.transparent
                )
            )

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        querySearch = query
                        page = 1
                        viewModel.getMovieSearchList(querySearch, page).observe(this@MainActivity, getMovieSearchList)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.onActionViewCollapsed()
        } else {
            super.onBackPressed()
        }
    }
}
