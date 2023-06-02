package com.redhaputra.movieapp.common.ui.utils

/**
 * Define string util for any modified string
 */

object StringUtils {

    /**
     * Combine poster image response to base original image url
     */
    fun String.toPosterImg(): String = "https://image.tmdb.org/t/p/w500${this}"
}