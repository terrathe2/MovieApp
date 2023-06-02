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
import com.redhaputra.movieapp.common.ui.base.BasePagingAdapter
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.common.ui.model.MovieListType
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
    private val nowPlayingAdapter by lazy {
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
        setupNowPlayingPagingListener()
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

        viewModel.nowPlayingMovieList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                nowPlayingAdapter.submitData(it)
            }
        }
    }

    private fun setupPagingAdapter() {
        val popularLayoutManager = PeekingLinearLayoutManager(requireContext(), HORIZONTAL, false)
        val topRatedLayoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        val nowPlayingLayoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)
        // popular
        viewBinding.rvPopularMovie.layoutManager = popularLayoutManager
        viewBinding.rvPopularMovie.adapter = popularAdapter.withLoadStateFooter(
            MovieListLoadStateAdapter {
                popularAdapter.retry()
            }
        )
        // top rated
        viewBinding.rvTopRatedMovie.layoutManager = topRatedLayoutManager
        viewBinding.rvTopRatedMovie.adapter = topRatedAdapter.withLoadStateFooter(
            MovieListLoadStateAdapter {
                topRatedAdapter.retry()
            }
        )
        // now playing
        viewBinding.rvNowPlayingMovie.layoutManager = nowPlayingLayoutManager
        viewBinding.rvNowPlayingMovie.adapter = nowPlayingAdapter.withLoadStateFooter(
            MovieListLoadStateAdapter {
                nowPlayingAdapter.retry()
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
                    toggleLoading(MovieListType.POPULAR, true)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            popularAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    toggleLoading(MovieListType.POPULAR, false)
                    toggleLayout(MovieListType.POPULAR, popularAdapter)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            popularAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Error }
                .collect { loadState ->
                    toggleLoading(MovieListType.POPULAR, false)
                    toggleLayout(MovieListType.POPULAR, popularAdapter)
                    showError(loadState)
                }
        }
    }

    private fun setupTopRatedPagingListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            topRatedAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.Loading && topRatedAdapter.itemCount == 0
                }
                .collect {
                    toggleLoading(MovieListType.TOP_RATED, true)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            topRatedAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    toggleLoading(MovieListType.TOP_RATED, false)
                    toggleLayout(MovieListType.TOP_RATED, topRatedAdapter)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            topRatedAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Error }
                .collect { loadState ->
                    toggleLoading(MovieListType.TOP_RATED, false)
                    toggleLayout(MovieListType.TOP_RATED, topRatedAdapter)
                    showError(loadState)
                }
        }
    }

    private fun setupNowPlayingPagingListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            nowPlayingAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.Loading && topRatedAdapter.itemCount == 0
                }
                .collect {
                    toggleLoading(MovieListType.NOW_PLAYING, true)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            nowPlayingAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    toggleLoading(MovieListType.NOW_PLAYING, false)
                    toggleLayout(MovieListType.NOW_PLAYING, nowPlayingAdapter)
                }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            nowPlayingAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Error }
                .collect { loadState ->
                    toggleLoading(MovieListType.NOW_PLAYING, false)
                    toggleLayout(MovieListType.NOW_PLAYING, nowPlayingAdapter)
                    showError(loadState)
                }
        }
    }

    private fun toggleLoading(type: MovieListType, visible: Boolean) {
        with(viewBinding) {
            when (type) {
                MovieListType.POPULAR -> {
                    viewBinding.popularLoading.frameLoading.isVisible = visible
                    viewBinding.rvPopularMovie.isVisible = !visible
                }
                MovieListType.TOP_RATED -> {
                    topRatedLoading.frameLoading.isVisible = visible
                    rvTopRatedMovie.isVisible = !visible
                }
                MovieListType.NOW_PLAYING -> {
                    nowPlayingLoading.frameLoading.isVisible = visible
                    rvNowPlayingMovie.isVisible = !visible
                }
            }
        }
    }

    private fun toggleLayout(type: MovieListType, adapter: BasePagingAdapter<MovieData>) {
        when (type) {
            MovieListType.POPULAR -> viewBinding.rvPopularMovie.isVisible = true
            MovieListType.TOP_RATED -> viewBinding.rvTopRatedMovie.isVisible = true
            MovieListType.NOW_PLAYING -> viewBinding.rvNowPlayingMovie.isVisible = true
        }

        val emptyReport = adapter.snapshot().items.isEmpty()
        adapter.setEmptyState(emptyReport)
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