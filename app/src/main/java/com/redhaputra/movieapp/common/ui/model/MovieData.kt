package com.redhaputra.movieapp.common.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * Movie Data model
 */
@Parcelize
@Keep
data class MovieData(
    val id: Int,
    val backImg: String,
    val posterImg: String,
    val overview: String,
    val title: String,
    val review: String? = ""
) : Parcelable