package com.muhghonim.stfp.activity.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.muhghonim.stfp.data.repository.NetworkState
import com.muhghonim.stfp.data.repository.details.MovieDetailsRepository
import com.muhghonim.stfp.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsViewModel (private val movieDetailsRepository: MovieDetailsRepository, movieId: Int) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val movieDetails : LiveData<MovieDetails> by lazy {
        movieDetailsRepository.fetctMovieDetails(compositeDisposable, movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieDetailsRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}