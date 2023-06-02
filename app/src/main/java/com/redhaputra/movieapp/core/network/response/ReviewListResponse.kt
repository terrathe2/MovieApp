package com.redhaputra.movieapp.core.network.response

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Review List Response data model
 */
@Keep
@JsonClass(generateAdapter = true)
data class ReviewListResponse(
    @Json(name = "page")
    val page: Int?,
    @Json(name = "results")
    val results: List<ItemReviewsResponse>?,
    @Json(name = "total_pages")
    val totalPages: Int?,
    @Json(name = "total_results")
    val totalResults: Int?
)