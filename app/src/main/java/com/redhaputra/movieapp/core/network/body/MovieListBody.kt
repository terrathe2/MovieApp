package com.redhaputra.movieapp.core.network.body

import androidx.annotation.Keep

/**
 * Movie list body data model
 */
@Keep
data class MovieListBody(
    val language: String = "en-US",
    val page: Int,
    val region: String = "ID"
)