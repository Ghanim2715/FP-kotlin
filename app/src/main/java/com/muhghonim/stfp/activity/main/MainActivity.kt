package com.muhghonim.stfp.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.muhghonim.stfp.R
import com.muhghonim.stfp.data.api.TheMovieDBClient
import com.muhghonim.stfp.data.api.TheMovieDBInterface
import com.muhghonim.stfp.data.repository.MovieAdapter
import com.muhghonim.stfp.data.repository.MovieRepository
import com.muhghonim.stfp.data.repository.NetworkState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieViewModel
    lateinit var movieRepository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MovieRepository(apiService)
        viewModel = getViewModel()
        val movieAdapter = MovieAdapter(this)

        val layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        rv_movie_now_playing.layoutManager = layoutManager
        rv_movie_now_playing.setHasFixedSize(true)
        rv_movie_now_playing.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_now_playing.visibility = if (viewModel.isEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            error_text_now_playing.visibility = if (viewModel.isEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.isEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }

    private fun getViewModel(): MovieViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(
                    movieRepository
                ) as T
            }
        })[MovieViewModel::class.java]
    }
}