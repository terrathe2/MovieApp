package com.redhaputra.movieapp.common.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.redhaputra.movieapp.common.ui.base.BaseViewHolder
import com.redhaputra.movieapp.databinding.EmptyListBinding

/**
 * Class describes movie list empty state view within the RecyclerView.
 *
 * @see BaseViewHolder
 */
class EmptyMovieListStateViewHolder(
    inflater: LayoutInflater,
    parent: ViewGroup,
) : BaseViewHolder<EmptyListBinding>(
    EmptyListBinding.inflate(inflater, parent, false)
)