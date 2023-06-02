package com.redhaputra.movieapp.core.network.repositories

import com.redhaputra.movieapp.common.ui.model.MovieListType
import com.redhaputra.movieapp.core.network.adapter.NetworkResponse
import com.redhaputra.movieapp.core.network.body.MovieListBody
import com.redhaputra.movieapp.core.network.response.MovieListResponse
import com.redhaputra.movieapp.core.network.services.MovieService
import java.io.IOException

/**
 * Repository module for handling Movie api response operations.
 */
class MovieRepository(
    private val service: MovieService
) {

    companion object {
        private const val CONNECTION_ERR = "Connection Error"
        private const val SERVER_ERR = "Server Error"
        private const val UNKNOWN_ERR = "Unknown Error"
    }

    /**
     * Get popular movie list response method handling
     *
     * @param params Parameter that needed for fetch Popular Movie List
     */
    suspend fun getMovieList(params: MovieListBody, type: MovieListType): NetworkResponse<MovieListResponse> {
        val queryMap = mapOf(
            "language" to params.language,
            "page" to params.page,
            "region" to params.region
        )

        try {
            val request = when (type) {
                MovieListType.POPULAR -> service.getPopularMovieList(queryMap)
                MovieListType.TOP_RATED -> service.getTopRatedMovieList(queryMap)
                MovieListType.NOW_PLAYING -> service.getNowPlayingMovieList(queryMap)
            }

            if (request.isSuccessful) {
                val body = request.body()
                return NetworkResponse.Success(body)
            }

            throw Exception(UNKNOWN_ERR)
        } catch (e: Exception) {
            if (e is IOException) {
                return NetworkResponse.Error(CONNECTION_ERR)
            }

            return NetworkResponse.Error(SERVER_ERR)
        }
    }
}