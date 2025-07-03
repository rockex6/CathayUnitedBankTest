package com.rockex6.cathayunitedbanktest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rockex6.cathayunitedbanktest.databinding.BottomSheetBinding
import com.rockex6.cathayunitedbanktest.util.AnimationUtils


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
        setupAnimations()
    }

    private fun setupBottomSheetBehavior() {
        val dialog = dialog as? BottomSheetDialog
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it).apply {
                skipCollapsed = true
                peekHeight = 0
                halfExpandedRatio = 0.9f
                isDraggable = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
            // 監聽狀態變化，確保保持展開狀態
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED ||
                        newState == BottomSheetBehavior.STATE_HALF_EXPANDED) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
    }

    private fun initListener() {
        binding.tvSortDesc.setOnClickListener {
            AnimationUtils.clickFeedback(it)
            changeSortedCallback?.invoke(SortedEnum.DESC)
            dismissWithAnimation()
        }
        binding.tvSortAsc.setOnClickListener {
            AnimationUtils.clickFeedback(it)
            changeSortedCallback?.invoke(SortedEnum.ASC)
            dismissWithAnimation()
        }
    }

    private fun setupAnimations() {
        // 初始化時隱藏內容
        binding.tvTitle.alpha = 0f
        binding.tvSortDesc.alpha = 0f
        binding.tvSortAsc.alpha = 0f
        binding.dragHandle.alpha = 0f

        // 延遲顯示動畫
        binding.root.postDelayed({
            animateContentIn()
        }, 100)
    }

    private fun animateContentIn() {
        // 滑動指示器動畫
        AnimationUtils.fadeIn(binding.dragHandle, 200)

        // 標題動畫
        binding.root.postDelayed({
            AnimationUtils.fadeIn(binding.tvTitle, 300)
        }, 100)

        // 選項動畫
        binding.root.postDelayed({
            binding.tvSortDesc.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .start()
        }, 200)

        binding.root.postDelayed({
            binding.tvSortAsc.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .start()
        }, 300)

        // 設置初始位置
        binding.tvSortDesc.translationY = 50f
        binding.tvSortAsc.translationY = 50f
    }

    private fun dismissWithAnimation() {
        binding.tvSortDesc.animate().alpha(0f).setDuration(150).start()
        binding.tvSortAsc.animate().alpha(0f).setDuration(150).start()
        binding.tvTitle.animate().alpha(0f).setDuration(150).start()

        binding.root.postDelayed({
            dismiss()
        }, 150)
    }
}