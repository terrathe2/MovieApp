package com.redhaputra.movieapp.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingListener
import com.redhaputra.movieapp.common.ui.base.BaseViewHolder
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.databinding.ItemListPopularMovieBinding

/**
 * Class describes popular movie list item view within RecyclerView.
 *
 * @see BaseViewHolder
 */
class PopularMovieListViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val listener: MovieListPagingListener
) : BaseViewHolder<ItemListPopularMovieBinding>(
    ItemListPopularMovieBinding.inflate(inflater, parent, false)
) {
    /**
     * Bind view data binding variables.
     *
     * @param data popular movie list item.
     */
    fun bind(data: MovieData, glide: RequestManager) {
        binding.root.setOnClickListener {
            listener.onClick(data)
        }
        binding.ivPopularMovie.setPadding(0, 0, 0, 0)
        if (data.backImg.isNotEmpty()) {
            glide.load(data.backImg)
                .fitCenter()
                .placeholder(R.drawable.ic_empty_movie_img_24)
                .error(R.drawable.ic_empty_movie_img_24)
                .into(binding.ivPopularMovie)
        }
        binding.executePendingBindings()
    }
}