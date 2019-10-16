package com.example.investigate

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ScannerOverlayView(context: Context, attr: AttributeSet) : View(context, attr) {

    private var crWidth: Int = 0
    private var crHeight: Int = 0
    private var strokePath = Path()
    private var transparentPaint: Paint
    private lateinit var strokeFramePaint: Paint

    private val rectSize = context.resources.getDimension(R.dimen.overlay_frame_size)
    private val rectRadius = context.resources.getDimension(R.dimen.overlay_frame_radius)
    private val paddingStroke = context.resources.getDimension(R.dimen.overlay_frame_padding)
    private val widthStroke = context.resources.getDimension(R.dimen.stroke)

    var on: Float= 0f
    set(value) {
        field = value
        invalidate()
    }

    var of: Float= 0f
        set(value) {
            field = value
            invalidate()
        }

    var phase: Float= 0f
        set(value) {
            field = value
            invalidate()
        }


    init {
        transparentPaint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        crWidth = w
        crHeight = h
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        strokeFramePaint = Paint().apply {
            color = Color.parseColor("#8C8C8C")
            alpha = (0.5 * 255).toInt()
            style = Paint.Style.STROKE
            strokeWidth = widthStroke
            strokeCap = Paint.Cap.ROUND
            pathEffect = ComposePathEffect(
                DashPathEffect(floatArrayOf(on ,of), phase),
                CornerPathEffect(rectRadius)
            )
        }

        canvas.drawColor(Color.parseColor("#66000000"))
        val left: Float = (crWidth - rectSize) / 2
        val top: Float = (crHeight - rectSize) / 2
        val insideRect = RectF(left, top, left + rectSize, top + rectSize)
        canvas.drawRoundRect(insideRect, rectRadius, rectRadius, transparentPaint)
        val outterRect = RectF(
            left - widthStroke / 2,
            top - widthStroke / 2,
            left + rectSize + widthStroke / 2,
            top + rectSize + widthStroke / 2
        )
        canvas.drawRoundRect(outterRect, rectRadius, rectRadius, strokeFramePaint)
    }
}