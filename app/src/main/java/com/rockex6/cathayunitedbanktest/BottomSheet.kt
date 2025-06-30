package com.rockex6.cathayunitedbanktest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rockex6.cathayunitedbanktest.databinding.BottomSheetBinding


class BottomSheet : BottomSheetDialogFragment() {

    private var changeSortedCallback: ((SortedEnum) -> Unit)? = null

    companion object {
        fun newInstance(changeSortedCallback: (SortedEnum) -> Unit): BottomSheet {
            val fragment = BottomSheet()
            fragment.changeSortedCallback = changeSortedCallback
            return fragment
        }
    }

    private lateinit var binding: BottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 設置最小高度
        setupBottomSheetBehavior()
        initListener()
    }

    private fun setupBottomSheetBehavior() {
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)

            // 設置最小高度（例如：螢幕高度的 30%）
            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            var minHeight = (screenHeight * 0.1).toInt()
            activity?.let {
                val display = it.windowManager.defaultDisplay
                when (display.rotation) {
                    Surface.ROTATION_90, Surface.ROTATION_270 -> {
                        minHeight = (screenHeight * 0.2).toInt() // 30% 的螢幕高度
                    }
                }
            }

            // 設置最小高度
            it.minimumHeight = minHeight

            // 可選：設置初始狀態
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED

            // 可選：設置 peek 高度（部分顯示的高度）
            behavior.peekHeight = minHeight
        }
    }

    private fun initListener() {
        binding.tvSortDesc.setOnClickListener {
            changeSortedCallback?.invoke(SortedEnum.DESC)
            dismiss()
        }
        binding.tvSortAsc.setOnClickListener {
            changeSortedCallback?.invoke(SortedEnum.ASC)
            dismiss()
        }
    }
}