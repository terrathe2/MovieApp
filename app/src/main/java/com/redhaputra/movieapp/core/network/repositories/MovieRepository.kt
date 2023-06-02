package com.redhaputra.movieapp.core.network.repositories

import android.content.SharedPreferences
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.common.ui.model.MovieListType
import com.redhaputra.movieapp.core.network.adapter.NetworkResponse
import com.redhaputra.movieapp.core.network.body.MovieListBody
import com.redhaputra.movieapp.core.network.response.MovieListResponse
import com.redhaputra.movieapp.core.network.response.ReviewListResponse
import com.redhaputra.movieapp.core.network.services.MovieService
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException


/**
 * Repository module for handling Movie api response operations.
 */
class MovieRepository(
    private val sharedPref: SharedPreferences,
    private val moshi: Moshi,
    private val service: MovieService,
) {

    companion object {
        private const val CONNECTION_ERR = "Connection Error"
        private const val SERVER_ERR = "Server Error"
        private const val UNKNOWN_ERR = "Unknown Error"
        private const val FAVORITES_MOVIE_KEY = "FAVORITES_MOVIE_KEY"
    }

    /**
     * Get movie list response method handling based on movie list type
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

    /**
     * Get movie reviews response method handling
     *
     * @param movieId Movie ID that needed for fetch Movie Reviews
     */
    suspend fun getMovieReviews(movieId: Int): NetworkResponse<ReviewListResponse> {
        val queryMap = mapOf(
            "language" to "en-US",
            "page" to 1
        )

        try {
            val request = service.getMovieReviews(id = movieId, query = queryMap)
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

    /**
     * Save and edit list favorite movie to local
     */
    fun inputFavoritesMovie(list: MutableList<MovieData>?) =
        list?.let {
            val type = Types.newParameterizedType(MutableList::class.java, MovieData::class.java)
            val jsonList = moshi.adapter<MutableList<MovieData>>(type).toJson(it)
            sharedPref.edit()
                .putString(FAVORITES_MOVIE_KEY, jsonList)
                .commit()
        }

    /**
     * Load Favorites data
     */
    fun loadFavoritesMovie(): MutableList<MovieData>? {
        val type = Types.newParameterizedType(MutableList::class.java, MovieData::class.java)
        val jsonProfile = sharedPref.getString(FAVORITES_MOVIE_KEY, null) ?: return null
        return moshi.adapter<MutableList<MovieData>>(type).fromJson(jsonProfile)
    }
}