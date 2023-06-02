package com.redhaputra.movieapp.common.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Class for presenting Load state in a [RecyclerView]
 * Adapter for list load more
 * @see LoadStateAdapter
 */
class MovieListLoadStateAdapter(private val retryAction: () -> Unit) :
    LoadStateAdapter<NetworkStateItemViewHolder>(){

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder =
        NetworkStateItemViewHolder(
            retryAction,
            parent,
            LayoutInflater.from(parent.context)
        )
}