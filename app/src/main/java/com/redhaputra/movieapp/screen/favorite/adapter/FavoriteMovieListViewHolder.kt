package com.redhaputra.movieapp.screen.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingListener
import com.redhaputra.movieapp.common.ui.base.BaseViewHolder
import com.redhaputra.movieapp.common.ui.model.MovieData
import com.redhaputra.movieapp.databinding.ItemListFavMovieBinding

/**
 * Class describes favorite movie list item view within RecyclerView.
 *
 * @see BaseViewHolder
 */
class FavoriteMovieListViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val listener: MovieListPagingListener
) : BaseViewHolder<ItemListFavMovieBinding>(
    ItemListFavMovieBinding.inflate(inflater, parent, false)
) {
    /**
     * Bind view data binding variables.
     *
     * @param data favorite movie list item.
     */
    fun bind(data: MovieData, glide: RequestManager) {
        binding.item = data
        binding.root.setOnClickListener {
            listener.onClick(data)
        }
        binding.ivFavMovie.setPadding(0, 0, 0, 0)
        glide.load(data.posterImg)
            .centerCrop()
            .placeholder(R.drawable.ic_empty_movie_img_24)
            .error(R.drawable.ic_empty_movie_img_24)
            .into(binding.ivFavMovie)
        binding.executePendingBindings()
    }
}