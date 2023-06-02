package com.redhaputra.movieapp.screen.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.adapters.MovieListLoadStateAdapter
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingAdapter
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingListener
import com.redhaputra.movieapp.common.ui.adapters.PeekingLinearLayoutManager
import com.redhaputra.movieapp.common.ui.base.BaseFragment
import com.redhaputra.movieapp.databinding.FragmentHomeBinding
import com.redhaputra.movieapp.screen.detail.DetailMovieFragment.Companion.MOVIE_ID_KEY
import com.redhaputra.movieapp.screen.home.adapter.PopularMoviePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * Home Screen [BaseFragment] subclass
 * to show list of popular, top rated, and now playing movie.
 */
@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding>(R.layout.fragment_home),
    MovieListPagingListener {

    private val viewModel: HomeViewModel by viewModels()
    private val popularAdapter by lazy {
        PopularMoviePagingAdapter(this, Glide.with(this)).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }
    private val topRatedAdapter by lazy {
        MovieListPagingAdapter(this, Glide.with(this)).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeClickAction()
        observeViewModel()
        setupPagingAdapter()
        setupPopularPagingListener()
        setupTopRatedPagingListener()
    }

    override fun onClick(movieId: Int?) {
        // go to detail movie
        safeToNavigate(R.id.action_homeFragment_to_detailMovieFragment) {
            findNavController().navigate(it, bundleOf(MOVIE_ID_KEY to movieId))
        }
    }

    private fun observeClickAction() {
        // go to favorite movie
        viewBinding.ivToolbarFavorite.setOnClickListener {
            safeToNavigate(R.id.action_homeFragment_to_favoriteMovieFragment) {
                findNavController().navigate(it)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.popularMovieList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                popularAdapter.submitData(it)
            }
        }

        viewModel.topRatedMovieList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                topRatedAdapter.submitData(it)
            }
        }
    }

    private fun setupPagingAdapter() {
        val peekLayoutManager = PeekingLinearLayoutManager(requireContext(), HORIZONTAL, false)
        val layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        viewBinding.rvPopularMovie.layoutManager = peekLayoutManager
        viewBinding.rvPopularMovie.adapter = popularAdapter.withLoadStateFooter(
            MovieListLoadStateAdapter {
                popularAdapter.retry()
            }
        )
        viewBinding.rvTopRatedMovie.layoutManager = layoutManager
        viewBinding.rvTopRatedMovie.adapter = topRatedAdapter.withLoadStateFooter(
            MovieListLoadStateAdapter {
                topRatedAdapter.retry()
            }
        )
    }

    private fun setupPopularPagingListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            popularAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.Loading && popularAdapter.itemCount == 0
                }
                .collect {
                    togglePopularLoading(true)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            popularAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    togglePopularLoading(false)
                    togglePopularLayout()
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            popularAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Error }
                .collect { loadState ->
                    togglePopularLoading(false)
                    togglePopularLayout()
                    showError(loadState)
                }
        }
    }

    private fun togglePopularLoading(visible: Boolean) {
        viewBinding.popularLoading.frameLoading.isVisible = visible
        viewBinding.rvPopularMovie.isVisible = !visible
    }

    private fun togglePopularLayout() {
        val emptyReport = popularAdapter.snapshot().items.isEmpty()

        viewBinding.rvPopularMovie.isVisible = true
        popularAdapter.setEmptyState(emptyReport)
    }

    private fun setupTopRatedPagingListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            topRatedAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.Loading && topRatedAdapter.itemCount == 0
                }
                .collect {
                    toggleTopRatedLoading(true)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            topRatedAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    toggleTopRatedLoading(false)
                    toggleTopRatedLayout()
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            topRatedAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Error }
                .collect { loadState ->
                    toggleTopRatedLoading(false)
                    toggleTopRatedLayout()
                    showError(loadState)
                }
        }
    }

    private fun toggleTopRatedLoading(visible: Boolean) {
        viewBinding.topRatedLoading.frameLoading.isVisible = visible
        viewBinding.rvTopRatedMovie.isVisible = !visible
    }

    private fun toggleTopRatedLayout() {
        val emptyReport = topRatedAdapter.snapshot().items.isEmpty()

        viewBinding.rvTopRatedMovie.isVisible = true
        topRatedAdapter.setEmptyState(emptyReport)
    }

    private fun showError(loadState: CombinedLoadStates) {
        val error = when {
            loadState.prepend is LoadState.Error ->
                loadState.prepend as LoadState.Error
            loadState.append is LoadState.Error ->
                loadState.append as LoadState.Error
            loadState.refresh is LoadState.Error ->
                loadState.refresh as LoadState.Error
            else -> null
        }
        error?.let {
            Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_SHORT).show()
        }
    }
}