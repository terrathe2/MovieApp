package com.redhaputra.movieapp.common.ui.adapters

import com.redhaputra.movieapp.common.ui.model.MovieData

/**
 * Listener for movie list
 */
interface MovieListPagingListener {
    /**
    * Action on clicked movie
    */
    fun onClick(movieData: MovieData?)
}