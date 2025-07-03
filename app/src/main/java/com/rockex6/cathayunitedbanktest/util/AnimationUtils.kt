package com.rockex6.cathayunitedbanktest.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import androidx.core.content.ContextCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.rockex6.cathayunitedbanktest.R

/**
 * 動畫工具類
 * 提供各種常用的動畫效果
 */
object AnimationUtils {

    /**
     * 淡入動畫
     */
    fun fadeIn(view: View, duration: Long = 300, onComplete: (() -> Unit)? = null) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onComplete?.invoke()
                }
            })
            .start()
    }

    /**
     * 淡出動畫
     */
    fun fadeOut(view: View, duration: Long = 300, onComplete: (() -> Unit)? = null) {
        view.animate()
            .alpha(0f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                    onComplete?.invoke()
                }
            })
            .start()
    }

    /**
     * 縮放動畫
     */
    fun scaleAnimation(view: View, fromScale: Float = 0.8f, toScale: Float = 1f, duration: Long = 200) {
        view.scaleX = fromScale
        view.scaleY = fromScale
        
        view.animate()
            .scaleX(toScale)
            .scaleY(toScale)
            .setDuration(duration)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    /**
     * 彈性動畫
     */
    fun springAnimation(view: View, property: DynamicAnimation.ViewProperty, finalPosition: Float) {
        val springAnimation = SpringAnimation(view, property, finalPosition)
        springAnimation.spring.stiffness = SpringForce.STIFFNESS_MEDIUM
        springAnimation.spring.dampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
        springAnimation.start()
    }

    /**
     * 顏色變化動畫
     */
    fun animateColorChange(
        view: View,
        fromColor: Int,
        toColor: Int,
        duration: Long = 300,
        onUpdate: (Int) -> Unit
    ) {
        val colorAnimator = ValueAnimator.ofArgb(fromColor, toColor)
        colorAnimator.duration = duration
        colorAnimator.addUpdateListener { animator ->
            val color = animator.animatedValue as Int
            onUpdate(color)
        }
        colorAnimator.start()
    }

    /**
     * 滑動進入動畫
     */
    fun slideInFromRight(view: View, duration: Long = 300) {
        view.translationX = view.width.toFloat()
        view.visibility = View.VISIBLE
        
        view.animate()
            .translationX(0f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    /**
     * 滑動退出動畫
     */
    fun slideOutToLeft(view: View, duration: Long = 300, onComplete: (() -> Unit)? = null) {
        view.animate()
            .translationX(-view.width.toFloat())
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = View.GONE
                    view.translationX = 0f
                    onComplete?.invoke()
                }
            })
            .start()
    }

    /**
     * 載入動畫資源
     */
    fun loadAnimation(context: Context, animRes: Int) = 
        AnimationUtils.loadAnimation(context, animRes)

    /**
     * 點擊反饋動畫
     */
    fun clickFeedback(view: View) {
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    /**
     * 搖擺動畫
     */
    fun shakeAnimation(view: View) {
        val shake = ObjectAnimator.ofFloat(view, "translationX", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f)
        shake.duration = 500
        shake.start()
    }
}
