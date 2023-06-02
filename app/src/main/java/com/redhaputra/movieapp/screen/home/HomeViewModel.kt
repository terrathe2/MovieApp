package com.redhaputra.movieapp.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingSource
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.common.ui.model.MovieListType.NOW_PLAYING
import com.redhaputra.movieapp.common.ui.model.MovieListType.POPULAR
import com.redhaputra.movieapp.common.ui.model.MovieListType.TOP_RATED
import com.redhaputra.movieapp.core.network.repositories.MovieRepository
import com.redhaputra.movieapp.core.network.response.ItemMoviesResponse
import com.redhaputra.movieapp.core.network.response.asExternalData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * View model responsible for preparing and managing the data for [HomeFragment].
 *
 * @see ViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private val pagingConfig = PagingConfig(PAGE_SIZE, initialLoadSize = PAGE_SIZE)
    val popularMovieList: LiveData<PagingData<MovieData>> =
        Pager(config = pagingConfig) {
            MovieListPagingSource(repository = repository, POPULAR)
        }.flow
            .map { it.map(ItemMoviesResponse::asExternalData) }
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
            .asLiveData()
    val topRatedMovieList: LiveData<PagingData<MovieData>> =
        Pager(config = pagingConfig) {
            MovieListPagingSource(repository = repository, TOP_RATED)
        }.flow
            .map { it.map(ItemMoviesResponse::asExternalData) }
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
            .asLiveData()
    val nowPlayingMovieList: LiveData<PagingData<MovieData>> =
        Pager(config = pagingConfig) {
            MovieListPagingSource(repository = repository, NOW_PLAYING)
        }.flow
            .map { it.map(ItemMoviesResponse::asExternalData) }
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
            .asLiveData()
}