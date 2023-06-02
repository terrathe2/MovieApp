package com.redhaputra.movieapp.common.ui.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Movie Data model
 */
@Parcelize
@Keep
@JsonClass(generateAdapter = true)
data class MovieData(
    @Json(name = "id")
    val id: Int,
    @Json(name = "backImg")
    val backImg: String,
    @Json(name = "posterImg")
    val posterImg: String,
    @Json(name = "overview")
    val overview: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "review")
    val review: String? = ""
) : Parcelable