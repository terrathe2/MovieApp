package com.redhaputra.movieapp.screen.favorite.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.redhaputra.movieapp.R
import com.redhaputra.movieapp.common.ui.adapters.EmptyMovieListStateViewHolder
import com.redhaputra.movieapp.common.ui.adapters.MovieListPagingListener
import com.redhaputra.movieapp.common.ui.model.MovieData

/**
 * Class for presenting Favorite Movie List data in a [RecyclerView], including computing
 * diffs between Lists on a background thread.
 * @param listener Listener for movie item
 *
 * @see PagingDataAdapter
 */
class FavoriteMovieListAdapter(
    private val listener: MovieListPagingListener,
    private val requestManager: RequestManager
) : ListAdapter<MovieData, RecyclerView.ViewHolder>(MOVIE_COMPARATOR) {

    companion object {
        val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieData>() {
            override fun areItemsTheSame(oldItem: MovieData, newItem: MovieData) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieData, newItem: MovieData) =
                oldItem == newItem
        }
    }

    private var showEmptyStateRow = false

    override fun getItemCount(): Int = super.getItemCount() + if (showEmptyStateRow) 1 else 0

    override fun getItemViewType(position: Int): Int =
        if (showEmptyStateRow) {
            R.layout.empty_list
        } else {
            R.layout.item_list_fav_movie
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_list_fav_movie ->
                FavoriteMovieListViewHolder(LayoutInflater.from(parent.context), parent, listener)
            R.layout.empty_list ->
                EmptyMovieListStateViewHolder(LayoutInflater.from(parent.context), parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_list_fav_movie -> {
                if (holder is FavoriteMovieListViewHolder) {
                    getItem(position)?.run {
                        holder.bind(this, requestManager)
                    }
                }
            }
            R.layout.empty_list -> holder as EmptyMovieListStateViewHolder
        }
    }

    /**
     * Handle set empty state
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setEmptyState(value: Boolean) {
        if (showEmptyStateRow != value) {
            showEmptyStateRow = value
            notifyDataSetChanged()
        }
    }
}