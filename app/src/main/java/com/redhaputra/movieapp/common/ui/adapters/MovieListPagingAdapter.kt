package com.redhaputra.movieapp.common.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.base.BasePagingAdapter
import com.redhaputra.movieapp.core.network.response.ItemMoviesResponse

/**
 * Class for presenting Common Movie List data in a [RecyclerView], including computing
 * diffs between Lists on a background thread.
 * @param listener Listener for movie item
 *
 * @see PagingDataAdapter
 */
class MovieListPagingAdapter (
    private val listener: MovieListPagingListener,
    private val requestManager: RequestManager
) : BasePagingAdapter<ItemMoviesResponse>(
    layoutId = R.layout.item_list_movie,
    itemsSame = { old, new -> old.id == new.id },
    contentsSame = { old, new -> old == new }
) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_list_movie ->
                if (holder is MovieListViewHolder) {
                    getItem(position)?.run {
                        holder.bind(this, requestManager)
                    }
                }

            R.layout.empty_list -> holder as EmptyMovieListStateViewHolder
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_list_movie -> MovieListViewHolder(inflater, parent, listener)
            R.layout.empty_list -> EmptyMovieListStateViewHolder(inflater, parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
}