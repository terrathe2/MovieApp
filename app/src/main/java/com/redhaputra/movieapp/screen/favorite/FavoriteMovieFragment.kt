package com.redhaputra.movieapp.screen.favorite

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingListener
import com.redhaputra.movieapp.common.ui.base.BaseFragment
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.databinding.FragmentFavoriteMovieBinding
import com.redhaputra.movieapp.screen.detail.DetailMovieFragment
import com.redhaputra.movieapp.screen.favorite.adapter.FavoriteMovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Favorite Movie Screen [BaseFragment] subclass
 * to show list of favorite movie.
 */
@AndroidEntryPoint
class FavoriteMovieFragment :
    BaseFragment<FragmentFavoriteMovieBinding>(R.layout.fragment_favorite_movie),
    MovieListPagingListener {

    private val viewModel: FavoriteMovieViewModel by viewModels()
    private val adapter by lazy {
        FavoriteMovieListAdapter(this, Glide.with(this)).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        observeClickAction()
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()
        // reset fav movie here, just in case detail movie accessed from fav movie list and
        // favorite data updated
        viewModel.setFavoritesMovie()
    }

    override fun onClick(movieData: MovieData?) {
        // go to detail movie
        safeToNavigate(R.id.action_favFragment_to_detailMovieFragment) {
            findNavController().navigate(it, bundleOf(DetailMovieFragment.MOVIE_DATA_KEY to movieData))
        }
    }

    private fun setupListAdapter() {
        viewBinding.rvFavMovie.layoutManager = LinearLayoutManager(requireContext())
        viewBinding.rvFavMovie.adapter = adapter
    }

    private fun observeClickAction() {
        viewBinding.ivFavToolbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeViewModel() {
        viewModel.favoritesMovie.observe(viewLifecycleOwner) {
            adapter.setEmptyState(it.isNullOrEmpty())
            adapter.submitList(it)
        }
    }
}