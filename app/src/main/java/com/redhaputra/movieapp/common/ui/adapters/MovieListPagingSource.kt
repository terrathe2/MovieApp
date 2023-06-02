package com.redhaputra.movieapp.common.ui.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.redhaputra.movieapp.common.ui.model.MovieListType
import com.redhaputra.movieapp.core.network.adapter.NetworkResponse
import com.redhaputra.movieapp.core.network.body.MovieListBody
import com.redhaputra.movieapp.core.network.repositories.MovieRepository
import com.redhaputra.movieapp.core.network.response.ItemMoviesResponse

/**
 * class that used to handle pagination of Movie List
 *
 * @see PagingSource
 */
class MovieListPagingSource(
    private val repository: MovieRepository,
    private val movieListType: MovieListType
) : PagingSource<Int, ItemMoviesResponse>() {

    companion object {
        // maximum page that can be requested from API
        private const val MAX_PAGE = 500
    }

    override fun getRefreshKey(state: PagingState<Int, ItemMoviesResponse>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemMoviesResponse> {
        val pageNumber = params.key ?: 1
        // Since 1 is the lowest page number, return null to signify no more pages should
        // be loaded before it.
        val prevKey = if (pageNumber <= 1) null else pageNumber - 1
        val param = MovieListBody(page = pageNumber)
        return when (val response = repository.getMovieList(param, movieListType)) {
            is NetworkResponse.Success -> {
                val totalPages = response.data?.totalPages ?: 1
                val data = response.data?.results ?: listOf()
                val nextKey =
                    if (data.isNotEmpty() && pageNumber < totalPages && pageNumber < MAX_PAGE) {
                        pageNumber + 1
                    } else {
                        null
                    }
                LoadResult.Page(data, prevKey, nextKey)
            }
            is NetworkResponse.Error -> LoadResult.Error(Throwable(
                when (movieListType) {
                    MovieListType.POPULAR -> "Failed to get popular movie"
                    MovieListType.TOP_RATED -> "Failed to get top rated movie"
                    MovieListType.NOW_PLAYING -> "Failed to get now playing movie"
                }
            ))
        }
    }
}