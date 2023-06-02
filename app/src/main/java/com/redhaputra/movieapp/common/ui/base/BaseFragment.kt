package com.redhaputra.movieapp.common.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.redhaputra.movieapp.common.ui.extensions.autoClearBinding

/**
 * Base fragment to standardize and simplify initialization for this component.
 *
 * @param layoutId Layout resource reference identifier.
 * @see Fragment
 */
abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int
) : Fragment() {

    companion object {
        private const val DELAY_CLICK = 500L
    }

    var viewBinding: B by autoClearBinding()
    private var lastClick = 0L

    /**
     * Called to Initialize view data binding variables when fragment view is created.
     */
    open fun onInitDataBinding() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onInitDataBinding()
    }

    protected fun safeToNavigate(targetDestination: Int? = null, safeAction: (Int) -> Unit) {
        val availableDestination = if (targetDestination != null) {
            findNavController().currentDestination?.getAction(targetDestination) != null
        } else {
            true
        }
        if (System.currentTimeMillis() - lastClick > DELAY_CLICK && availableDestination) {
            lastClick = System.currentTimeMillis()
            if (targetDestination != null) {
                safeAction(targetDestination)
            } else {
                safeAction(0) // set '0' as default action
            }
        }
    }
}