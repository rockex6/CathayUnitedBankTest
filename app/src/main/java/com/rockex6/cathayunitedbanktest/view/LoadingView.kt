package com.rockex6.cathayunitedbanktest.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.rockex6.cathayunitedbanktest.R

/**
 * 自定義載入動畫 View
 * 顯示三個跳動的圓點載入效果
 */
class LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dots = mutableListOf<Dot>()
    private var animator: ValueAnimator? = null
    
    private val dotRadius = 12f
    private val dotSpacing = 40f
    private val animationDuration = 1200L
    
    init {
        paint.color = ContextCompat.getColor(context, android.R.color.holo_blue_bright)
        
        // 創建三個圓點
        for (i in 0..2) {
            dots.add(Dot(i * dotSpacing, 0f, dotRadius))
        }
        
        startAnimation()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = (dotRadius * 2 + dotSpacing * 2).toInt()
        val height = (dotRadius * 4).toInt()
        setMeasuredDimension(width, height)
    }
    
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val centerX = width / 2f
        val centerY = height / 2f
        
        dots.forEachIndexed { index, dot ->
            val x = centerX - dotSpacing + index * dotSpacing
            val y = centerY + dot.offsetY
            
            paint.alpha = (255 * dot.alpha).toInt()
            canvas.drawCircle(x, y, dot.radius, paint)
        }
    }
    
    private fun startAnimation() {
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = animationDuration
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                
                dots.forEachIndexed { index, dot ->
                    val delay = index * 0.2f
                    val adjustedProgress = ((progress + delay) % 1f)
                    
                    // 計算跳動效果
                    val bounceProgress = if (adjustedProgress < 0.5f) {
                        adjustedProgress * 2f
                    } else {
                        2f - adjustedProgress * 2f
                    }
                    
                    dot.offsetY = -bounceProgress * 30f
                    dot.alpha = 0.3f + bounceProgress * 0.7f
                    dot.radius = dotRadius * (0.8f + bounceProgress * 0.2f)
                }
                
                invalidate()
            }
        }
        animator?.start()
    }
    
    fun stopAnimation() {
        animator?.cancel()
        animator = null
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }
    
    private data class Dot(
        val baseX: Float,
        var offsetY: Float,
        var radius: Float,
        var alpha: Float = 1f
    )
}
