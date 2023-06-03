package com.redhaputra.movieapp.screen.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.core.network.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * View model responsible for preparing and managing the data for [FavoriteMovieFragment].
 *
 * @see ViewModel
 */
@HiltViewModel
class FavoriteMovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _favoritesMovie = MutableLiveData<List<MovieData>>()
    val favoritesMovie: LiveData<List<MovieData>> = _favoritesMovie

    /**
     * Method to set favorites moview from local
     */
    fun setFavoritesMovie() {
        _favoritesMovie.value = repository.loadFavoritesMovie() ?: mutableListOf()
    }
}