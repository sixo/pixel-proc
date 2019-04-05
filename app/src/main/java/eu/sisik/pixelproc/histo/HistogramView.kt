package eu.sisik.pixelproc.histo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Copyright (c) 2019 by Roman Sisik. All rights reserved.
 */
class HistogramView(context: Context, attrs: AttributeSet?): View(context, attrs) {

    var maxRange = 255

    var reds: IntArray? = null
    var greens: IntArray? = null
    var blues: IntArray? = null
    var lumas: IntArray? = null


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        reds?.let { drawHistogram(it, canvas, Color.RED) }
        greens?.let { drawHistogram(it, canvas, Color.GREEN) }
        blues?.let { drawHistogram(it, canvas, Color.BLUE) }
        lumas?.let { drawHistogram(it, canvas, Color.WHITE) }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var contentWidth = if (suggestedMinimumWidth > 256) suggestedMinimumWidth else 256
        var contentHeight = if (suggestedMinimumHeight > 256) suggestedMinimumHeight else 256

        val desiredWidth = contentWidth + paddingLeft + paddingRight
        val desiredHeight = contentHeight + paddingTop + paddingBottom

        val widthSpecs = resolveSizeAndState(desiredWidth, widthMeasureSpec, 0)
        val heightSpecs = resolveSizeAndState(desiredHeight, heightMeasureSpec, 0)

        setMeasuredDimension(widthSpecs, heightSpecs)
    }

    private fun drawHistogram(vals: IntArray, canvas: Canvas?, color: Int) {
        val paint = Paint().apply {
            this.color = color
            style = Paint.Style.STROKE
            strokeWidth = 3f
        }

        Path().run {
            moveTo(paddingLeft.toFloat(), height - paddingBottom.toFloat())

            val graphWidth = width - paddingLeft - paddingRight
            val graphHeight = height - paddingTop - paddingBottom

            for (i in 0 until vals!!.size) {
                val x = (graphWidth * i.toFloat() / vals!!.size) + paddingLeft
                val y = (graphHeight - (graphHeight * vals!![i].toFloat() / maxRange)) + paddingBottom // flip y

                lineTo(x, y)
            }

            canvas?.drawPath(this, paint)
        }
    }
}