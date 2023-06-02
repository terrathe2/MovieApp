package com.redhaputra.movieapp.common.ui.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.redhaputra.movieapp.R


/**
 * Base list adapter to standardize and simplify initialization for this component.
 * with empty state
 *
 * @param layoutId  Id of item layout needed to return
 * @param itemsSame Function called to check whether two objects represent the same item.
 * @param contentsSame Function called to check whether two items have the same data.
 * @see PagingDataAdapter
 */
abstract class BasePagingAdapter<T : Any>(
    @LayoutRes val layoutId: Int,
    itemsSame: (T, T) -> Boolean,
    contentsSame: (T, T) -> Boolean
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = itemsSame(oldItem, newItem)
    override fun areContentsTheSame(oldItem: T, newItem: T) = contentsSame(oldItem, newItem)
}) {
    private var showEmptyStateRow = false

    /**
     * Called when RecyclerView needs a new [RecyclerView.ViewHolder] of the given type to
     * represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param inflater Instantiates a layout XML file into its corresponding View objects.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see PagingDataAdapter.onCreateViewHolder
     */
    abstract fun onCreateViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater,
        viewType: Int
    ): RecyclerView.ViewHolder

    /**
     * Called when RecyclerView needs a new [RecyclerView.ViewHolder] of the given type to
     * represent an item.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see PagingDataAdapter.onCreateViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        onCreateViewHolder(
            parent = parent,
            inflater = LayoutInflater.from(parent.context),
            viewType = viewType
        )

    override fun getItemCount(): Int = super.getItemCount() + if (showEmptyStateRow) 1 else 0

    override fun getItemViewType(position: Int): Int =
        if (showEmptyStateRow) {
            R.layout.empty_list
        } else {
            layoutId
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