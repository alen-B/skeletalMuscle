package com.fjp.skeletalmuscle.app.weight

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private var bitmapShader: BitmapShader? = null
    private val matrix = Matrix()

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable
        if (drawable == null) {
            return
        }

        val bitmap = drawableToBitmap(drawable)
        setupShader(bitmap)

        canvas.drawCircle(width / 2f, height / 2f, Math.min(width, height) / 2f, paint)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, width, height)
            drawable.draw(canvas)
            bitmap
        }
    }

    private fun setupShader(bitmap: Bitmap) {
        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        matrix.setScale(width / bitmap.width.toFloat(), height / bitmap.height.toFloat())
        bitmapShader?.setLocalMatrix(matrix)
        paint.shader = bitmapShader
    }
}