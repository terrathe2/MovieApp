package com.redhaputra.movieapp.core.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Item of Review List Response data model
 */
@Keep
@JsonClass(generateAdapter = true)
data class ItemReviewsResponse(
    @Json(name = "author")
    val author: String?,
    @Json(name = "content")
    val content: String?,
    @Json(name = "id")
    val id: String?
)