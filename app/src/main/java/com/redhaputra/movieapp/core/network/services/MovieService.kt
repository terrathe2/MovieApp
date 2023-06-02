package com.redhaputra.movieapp.core.network.services

import com.redhaputra.movieapp.core.network.response.MovieListResponse
import com.redhaputra.movieapp.core.network.response.ReviewListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Representation interface of the MovieDB API endpoints.
 */
interface MovieService {
    /**
     * Get API Service for Popular Movie list
     */
    @GET("movie/popular")
    @JvmSuppressWildcards
    suspend fun getPopularMovieList(
        @QueryMap query: Map<String, Any>
    ): Response<MovieListResponse>

    /**
     * Get API Service for Top Rated Movie list
     */
    @GET("movie/top_rated")
    @JvmSuppressWildcards
    suspend fun getTopRatedMovieList(
        @QueryMap query: Map<String, Any>
    ): Response<MovieListResponse>

    /**
     * Get API Service for Now Playing Movie list
     */
    @GET("movie/now_playing")
    @JvmSuppressWildcards
    suspend fun getNowPlayingMovieList(
        @QueryMap query: Map<String, Any>
    ): Response<MovieListResponse>

    /**
     * Get API Service for Movie Review
     */
    @GET("movie/{id}/reviews")
    @JvmSuppressWildcards
    suspend fun getMovieReviews(
        @Path("id") id: Int,
        @QueryMap query: Map<String, Any>
    ): Response<ReviewListResponse>
}