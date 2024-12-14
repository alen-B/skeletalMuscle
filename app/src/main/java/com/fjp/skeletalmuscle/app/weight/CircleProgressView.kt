package com.fjp.skeletalmuscle.app.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.lang.Integer.min

/**
 *Author:Mr'x
 *Time:2024/11/24
 *Description:
 */
class CircleProgressView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var circlePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        pathEffect = CornerPathEffect(10f)

    }
    private var textPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }
    private var score = 0
    private var oval = RectF()
    private var widthSize = 0
    private var heightSize = 0
    private var backgroundCircleColor = Color.parseColor("#f6f6f6")
    private var circleColor = Color.parseColor("#ffc019")
    private var textColor = Color.parseColor("#ffc019")
    private var textSize = 115
    fun setScore(score: Int) {
        this.score = score
        invalidate()
    }

    fun setCircleColor(circleColor: Int) {
        this.circleColor = circleColor
        invalidate()
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        invalidate()
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        widthSize = measuredWidth
        heightSize = measuredHeight
        val size = min(widthSize, heightSize)
        setMeasuredDimension(size, size)

        oval.set(20f, 20f, size.toFloat() - 20, size.toFloat() - 20)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        circlePaint.color = backgroundCircleColor
        circlePaint.strokeWidth = 15f

        textPaint.color = textColor
        textPaint.textSize = textSize.toFloat()
        // 绘制圆形背景（灰色的圆环）
        canvas.drawArc(oval, 0f, 360f, false, circlePaint)

        // 根据分数计算角度
        val angle = (score / 100f) * 360f

        // 绘制表示进度的圆弧（根据分数的彩色圆弧）
        circlePaint.color = circleColor
        canvas.drawArc(oval, -90f, angle, false, circlePaint)

        // 获取文字的高度，用于垂直居中显示文字
        val fontMetrics = textPaint.fontMetricsInt
        val baseline = (heightSize - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top
        // 绘制中间的数字
        canvas.drawText(score.toString(), widthSize / 2f, baseline.toFloat(), textPaint)
    }
}