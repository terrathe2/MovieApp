package com.redhaputra.movieapp.core.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Movie List Response data model
 */
@Keep
@JsonClass(generateAdapter = true)
data class MovieListResponse(
    @Json(name = "page")
    val page: Int?,
    @Json(name = "results")
    val results: List<ItemMoviesResponse>?,
    @Json(name = "total_pages")
    val totalPages: Int?,
    @Json(name = "total_results")
    val totalResults: Int?
)
