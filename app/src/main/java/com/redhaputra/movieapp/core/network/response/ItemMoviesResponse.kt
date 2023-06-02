package com.redhaputra.movieapp.core.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Item of Movie List Response data model
 */
@Keep
@JsonClass(generateAdapter = true)
data class ItemMoviesResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "backdrop_path")
    val backImg: String,
    @Json(name = "poster_path")
    val posterImg: String,
    @Json(name = "overview")
    val overview: String,
    @Json(name = "title")
    val title: String
)
