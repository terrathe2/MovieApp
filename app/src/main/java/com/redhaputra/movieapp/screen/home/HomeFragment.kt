package com.redhaputra.movieapp.screen.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.adapters.MovieListLoadStateAdapter
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingListener
import com.redhaputra.movieapp.common.ui.adapters.PeekingLinearLayoutManager
import com.redhaputra.movieapp.common.ui.base.BaseFragment
import com.redhaputra.movieapp.databinding.FragmentHomeBinding
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setupPagingAdapter()
        setupPopularPagingListener()
    }

    override fun onClick(movieId: Int?) {
    }

    private fun setupPagingAdapter() {
        val layoutManager = PeekingLinearLayoutManager(requireContext(), HORIZONTAL, false)
        viewBinding.rvPopularMovie.layoutManager = layoutManager
        viewBinding.rvPopularMovie.adapter = popularAdapter.withLoadStateFooter(
            MovieListLoadStateAdapter {
                popularAdapter.retry()
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

    private fun observeViewModel() {
        viewModel.popularMovieList.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                popularAdapter.submitData(it)
            }
        }
    }
}