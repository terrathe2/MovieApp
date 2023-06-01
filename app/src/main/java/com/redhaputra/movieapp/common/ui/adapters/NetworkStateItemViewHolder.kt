package com.redhaputra.movieapp.common.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.redhaputra.movieapp.common.ui.base.BaseViewHolder
import com.redhaputra.movieapp.databinding.ItemNetworkStateBinding

/**
 * Class describes list network state view within the recyclerview.
 *
 * @see BaseViewHolder
 */
class NetworkStateItemViewHolder(
    private val retry: () -> Unit,
    parent: ViewGroup,
    inflater: LayoutInflater,
) : BaseViewHolder<ItemNetworkStateBinding>(
    ItemNetworkStateBinding.inflate(inflater, parent, false)
) {
    /**
     * func to Bind view data binding variables.
     *
     * @param loadState Load state.
     */
    fun bind(loadState: LoadState) {
        binding.progressErrorLoadmore.isVisible = loadState is LoadState.Loading
        binding.ivLoadMore.isVisible = loadState is LoadState.Error
        binding.ivLoadMore.setOnClickListener {
            retry()
        }
    }
}