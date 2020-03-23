package com.anuj55149.circularprogressbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.RectF
import android.graphics.Paint
import android.content.res.TypedArray
import android.graphics.Canvas
import kotlin.math.min
import android.animation.ObjectAnimator
import android.view.animation.*
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin

class CircularProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr){

    private val startAngle = -90F
    private val strokeWidth = 30F
    private val blobRadius = 40F
    private lateinit var rectF: RectF
    private lateinit var backgroundPaint: Paint
    private lateinit var foregroundPaint: Paint
    private lateinit var blobPaint: Paint

    private var progress: Float = 0F
        set(value) {
            field = value
            invalidate()
        }

    init {
        attrs?.let {
            parseAttributes(context, it)
        }
    }

    private fun parseAttributes(context: Context, attrs: AttributeSet) {
        rectF = RectF()
        val typedArray: TypedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircularProgressBar,
            0, 0
        )
        try {
            progress = typedArray.getFloat(R.styleable.CircularProgressBar_optionProgress, progress)
        } finally {
            typedArray.recycle()
        }

        backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        foregroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        blobPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        backgroundPaint.color =  ContextCompat.getColor(context, R.color.progressBackground)
        backgroundPaint.style = Paint.Style.STROKE
        backgroundPaint.strokeWidth = strokeWidth
        foregroundPaint.color =  ContextCompat.getColor(context, R.color.progressForeground)
        foregroundPaint.style = Paint.Style.STROKE
        foregroundPaint.strokeWidth = strokeWidth
        foregroundPaint.strokeCap = Paint.Cap.ROUND
        blobPaint.color = ContextCompat.getColor(context, R.color.blob)
        blobPaint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.drawOval(rectF, backgroundPaint)
        val ovalRadius = rectF.width() / 2
        val angle = 360 * progress / MAX
        canvas?.drawArc(rectF, startAngle, angle, false, foregroundPaint)

        canvas?.drawCircle(rectF.width() / 2 + blobRadius + ovalRadius * sin(Math.toRadians(angle.toDouble())).toFloat(),
            blobRadius + ovalRadius - ovalRadius * cos(Math.toRadians(angle.toDouble())).toFloat(),
            blobRadius,
            blobPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        val width = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val min = min(width, height)
        setMeasuredDimension(min, min)
        rectF.set(blobRadius, blobRadius,
            min - blobRadius, min - blobRadius)
    }

    fun setProgressWithAnimation(progress: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress)
        objectAnimator.duration = (500 + progress / MAX * 1500).toLong()
        objectAnimator.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator.start()
    }

    companion object {
        private const val MAX = 100
    }
}
