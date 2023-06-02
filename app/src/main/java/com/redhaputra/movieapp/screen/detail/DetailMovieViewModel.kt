package com.redhaputra.movieapp.screen.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.core.network.adapter.NetworkResponse
import com.redhaputra.movieapp.core.network.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * View model responsible for preparing and managing the data for [DetailMovieFragment].
 *
 * @see ViewModel
 */
@HiltViewModel
class DetailMovieViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movieData = MutableLiveData<MovieData>()
    val movieData: LiveData<MovieData> = _movieData

    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isRefreshing = MutableLiveData(false)
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent.asSharedFlow()

    /**
     * Handle set movie id for fetch body
     */
    fun setMovieData(movieData: MovieData) {
        _movieData.value = movieData
    }

    /**
     * Handle pokemon detail api fetch
     */
    fun getMovieReviews(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getMovieReviews(id)
            withContext(Dispatchers.Main) {
                when (response) {
                    is NetworkResponse.Success -> {
                        response.data?.results?.let { reviews ->
                            val review = if (reviews.isNotEmpty()) {
                                reviews.first().content
                            } else {
                                "-"
                            }
                            _movieData.value = _movieData.value?.copy(review = review)
                        }
                    }
                    is NetworkResponse.Error -> _errorEvent.emit(response.message)
                }
                _isLoading.value = false
                _isRefreshing.value = false
            }
        }
    }

    /**
     * Refresh list report data
     */
    fun onRefresh() {
        _isRefreshing.value = true
        viewModelScope.launch {
            getMovieReviews(_movieData.value?.id ?: 0)
        }
    }
}