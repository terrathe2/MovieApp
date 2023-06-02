package com.redhaputra.movieapp.screen.detail

import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.base.BaseFragment
import com.redhaputra.movieapp.databinding.FragmentDetailMovieBinding

/**
 * Detail Movie Screen [BaseFragment] subclass
 * to show movie detail.
 */
class DetailMovieFragment :
    BaseFragment<FragmentDetailMovieBinding>(R.layout.fragment_detail_movie) {

    companion object {
        const val MOVIE_ID_KEY = "MOVIE_ID_KEY"
    }
}