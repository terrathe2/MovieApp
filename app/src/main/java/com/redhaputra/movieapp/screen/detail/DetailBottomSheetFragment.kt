package com.redhaputra.movieapp.screen.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.redhaputra.movieapp.databinding.FragmentDetailBottomSheetBinding

/**
 * Dialog for share movie
 *
 * @see BottomSheetDialogFragment
 */
class DetailBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentDetailBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewAction()
    }

    private fun observeViewAction() {
        binding.layout1.setOnClickListener {
            showMsg("option 1")
            dismiss()
        }
        binding.layout2.setOnClickListener {
            showMsg("option 2")
            dismiss()
        }
        binding.layout3.setOnClickListener {
            showMsg("option 3")
            dismiss()
        }
    }

    private fun showMsg(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}