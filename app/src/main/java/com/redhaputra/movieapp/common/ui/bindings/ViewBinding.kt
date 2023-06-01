package com.redhaputra.movieapp.common.ui.bindings

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Simplification to check and setup view as visible.
 */
@set:BindingAdapter("visible")
inline var View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

/**
 * Simplification to check and setup view as gone.
 */
@set:BindingAdapter("gone")
var View.gone
    get() = visibility == View.GONE
    set(value) {
        visibility = if (value) View.GONE else View.VISIBLE
    }

/**
 * Simplification to check and setup view as invisible.
 */
@set:BindingAdapter("invisible")
var View.invisible
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }