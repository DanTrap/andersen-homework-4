package com.dantrap.andersen_homework_4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class AnalogClockView(context: Context, attrs: AttributeSet) :
    View(context, attrs) {

    private var clockHeight = 0
    private var clockWidth = 0
    private var padding = 0
    private var fontSize = 0
    private var numeralSpacing = 0
    private var handTruncation = 0
    private var hourHandTruncation = 0
    private var radius = 0
    private var isInit: Boolean = false
    private val numbers = arrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val paint = Paint()
    private val rect = Rect()

    override fun onDraw(canvas: Canvas) {
        if (!isInit) initClock()

        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)

        postInvalidateDelayed(500)
        invalidate()
    }

    private fun initClock() {
        clockHeight = height
        clockWidth = width
        padding = numeralSpacing + 50
        fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15F, resources.displayMetrics)
                .toInt()
        val min = clockHeight.coerceAtMost(clockWidth)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        isInit = true
    }

    private fun drawCircle(canvas: Canvas) {

        with (paint) {
            reset()
            color = resources.getColor(R.color.purple_500)
            strokeWidth = 8F
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        canvas.drawCircle(width / 2F, height / 2F, (radius + padding - 10).toFloat(), paint)
    }

    private fun drawCenter(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        canvas.drawCircle(width / 2F, height / 2F, 12F, paint)
    }

    private fun drawNumeral(canvas: Canvas) {
        paint.textSize = fontSize.toFloat()

        for (number in numbers) {
            val numberString = number.toString()
            paint.getTextBounds(numberString, 0, numberString.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            val x = (width / 2 + cos(angle) * radius - rect.width() / 2).toFloat()
            val y = (height / 2 + sin(angle) * radius + rect.width() / 2).toFloat()
            canvas.drawText(numberString, x, y, paint)
        }
    }

    private fun drawHands(canvas: Canvas) {
        val calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        drawHand(canvas, ((hour + calendar.get(Calendar.MINUTE) / 60) * 5).toDouble(), true)
        drawHand(canvas, calendar.get(Calendar.MINUTE).toDouble(), false)
        drawHand(canvas, calendar.get(Calendar.SECOND).toDouble(), false)
    }

    private fun drawHand(canvas: Canvas, loc: Double, isHour: Boolean) {
        val angle = Math.PI * loc / 30 - Math.PI / 2
        val handRadius =
            if (isHour) radius - handTruncation - hourHandTruncation else radius - handTruncation
        canvas.drawLine(
            width / 2F,
            height / 2F,
            (width / 2 + cos(angle) * handRadius).toFloat(),
            (height / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }

}