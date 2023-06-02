package com.redhaputra.movieapp.screen.detail

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.base.BaseFragment
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.databinding.FragmentDetailMovieBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Detail Movie Screen [BaseFragment] subclass
 * to show movie detail.
 */
@AndroidEntryPoint
class DetailMovieFragment :
    BaseFragment<FragmentDetailMovieBinding>(R.layout.fragment_detail_movie) {

    companion object {
        const val MOVIE_DATA_KEY = "MOVIE_DATA_KEY"
    }

    private val viewModel: DetailMovieViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
        observeViewAction()
        observeViewModel()
    }

    override fun onInitDataBinding() {
        viewBinding.viewModel = viewModel
    }

    @Suppress("deprecation")
    private fun initFragment() {
        val movieData = if (SDK_INT >= 33) {
            arguments?.getParcelable(MOVIE_DATA_KEY, MovieData::class.java)
        } else {
            arguments?.getParcelable(MOVIE_DATA_KEY) as MovieData?
        }
        movieData?.let {
            viewModel.setMovieData(it)
            viewModel.getMovieReviews(movieData.id)
        }
    }

    private fun observeViewAction() {
        viewBinding.ivDetailToolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.errorEvent.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.movieData.observe(viewLifecycleOwner) {
            Glide.with(requireContext())
                .load(it.backImg)
                .centerCrop()
                .placeholder(R.drawable.ic_empty_movie_img_24)
                .error(R.drawable.ic_empty_movie_img_24)
                .into(viewBinding.ivDetailMovie)
        }
    }

    private fun onBackPressed() {
        findNavController().popBackStack()
    }
}