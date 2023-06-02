package com.redhaputra.movieapp.common.ui.model

import androidx.annotation.Keep

/**
 * Movie Data model
 */
@Keep
data class MovieData(
    val id: Int,
    val backImg: String,
    val posterImg: String,
    val overview: String,
    val title: String
)