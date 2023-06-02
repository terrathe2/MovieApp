package com.redhaputra.movieapp.screen.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.adapters.EmptyMovieListStateViewHolder
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingListener
import com.redhaputra.movieapp.common.ui.base.BasePagingAdapter
import com.redhaputra.movieapp.common.ui.model.MovieData

/**
 * Class for presenting Popular Movie List data in a [RecyclerView], including computing
 * diffs between Lists on a background thread.
 * @param listener Listener for movie item
 *
 * @see PagingDataAdapter
 */
class PopularMoviePagingAdapter(
    private val listener: MovieListPagingListener,
    private val requestManager: RequestManager
) :
    BasePagingAdapter<MovieData>(
        layoutId = R.layout.item_list_popular_movie,
        itemsSame = { oldItem, newItem -> oldItem.id == newItem.id },
        contentsSame = { oldItem, newItem -> oldItem == newItem }
    ) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_list_popular_movie ->
                if (holder is PopularMovieListViewHolder) {
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
            R.layout.item_list_popular_movie ->
                PopularMovieListViewHolder(inflater, parent, listener)
            R.layout.empty_list -> EmptyMovieListStateViewHolder(inflater, parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
}