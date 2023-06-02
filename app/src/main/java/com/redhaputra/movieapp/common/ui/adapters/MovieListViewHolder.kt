package com.redhaputra.movieapp.common.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.RequestManager
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.base.BaseViewHolder
import com.redhaputra.movieapp.common.ui.utils.StringUtils.toPosterImg
import com.redhaputra.movieapp.core.network.response.ItemMoviesResponse
import com.redhaputra.movieapp.databinding.ItemListMovieBinding

/**
 * Class describes common movie list item view within RecyclerView.
 *
 * @see BaseViewHolder
 */
class MovieListViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
    private val listener: MovieListPagingListener
) : BaseViewHolder<ItemListMovieBinding>(
    ItemListMovieBinding.inflate(inflater, parent, false)
) {
    /**
     * Bind view data binding variables.
     *
     * @param data movie list item.
     */
    fun bind(data: ItemMoviesResponse, glide: RequestManager) {
        binding.item = data
        binding.root.setOnClickListener {
            listener.onClick(data.id)
        }
        binding.ivMovie.setPadding(0, 0, 0, 0)
        glide.load(data.posterImg.toPosterImg())
            .centerCrop()
            .placeholder(R.drawable.ic_empty_movie_img_24)
            .into(binding.ivMovie)
        binding.executePendingBindings()
    }
}