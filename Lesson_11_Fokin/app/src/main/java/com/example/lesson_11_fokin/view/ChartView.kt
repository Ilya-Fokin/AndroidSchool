package com.example.lesson_11_fokin.view

import android.animation.ValueAnimator
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.View.OnClickListener
import androidx.core.content.ContextCompat
import com.example.lesson_11_fokin.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.min

class ChartView : View, OnClickListener {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context, attributeSet)
    }

    private var dataMap = mutableMapOf<String, Int>()
    private var endListHeight = mutableListOf<Float>()
    private var startListHeight = mutableListOf<Float>()
    private var currentListHeight = mutableListOf<Float>()

    private var columnColor: Int = 0
    private var dateColor: Int = 0

    private val columnWidth by lazy { resources.getDimension(R.dimen.column_width) }
    private val columnLineMargin by lazy { resources.getDimension(R.dimen.column_line_margin) }
    private val columnTextSize by lazy { resources.getDimension(R.dimen.column_text_size) }
    private val columnCornerRadius by lazy { resources.getDimension(R.dimen.column_corner_radius) }
    private val defaultColumnHeight by lazy { resources.getDimension(R.dimen.column_default_height) }

    private var indentText: Float = 0F
    private var maxColumnHeight: Float = 0F

    private fun init(context: Context, attributeSet: AttributeSet? = null) {
        val attr = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.ChartView,
            0,
            0
        )

        setOnClickListener(this)

        try {
            columnColor = attr.getColor(
                R.styleable.ChartView_chartColumnColor,
                ContextCompat.getColor(context, R.color.yellow)
            )
            dateColor = attr.getColor(
                R.styleable.ChartView_chartDateColor,
                ContextCompat.getColor(context, R.color.grey)
            )
        } finally {
            attr.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = LayoutParams.MATCH_PARENT
        val desiredHeight = resources.getDimension(R.dimen.desired_height)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> min(desiredWidth, widthSize)
            else -> desiredWidth
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> min(desiredHeight.toInt(), heightSize)
            else -> desiredHeight
        }

        setMeasuredDimension(width, height.toInt())
    }

    fun setData(dataList: List<Int>) {
        if (dataList.size > 9) {
            return
        }
        val formatter = DateTimeFormatter.ofPattern("dd.MM")
        var days = dataList.size.toLong()
        dataList.forEach {
            val date = LocalDate.now().minusDays(days - 1).format(formatter)
            dataMap[date] = it
            days--
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (dataMap.isNotEmpty()) {
            indentText = ((width - getDatesWidth()) / (dataMap.size + 1)).toFloat()
        }
        maxColumnHeight =
            height.toFloat() - paddingBottom - columnLineMargin * 2 - columnTextSize * 2 - paddingTop
        dataMap.forEach { date ->
            currentListHeight.add(height.toFloat() - paddingBottom - getDateHeight(date.key) - columnLineMargin - defaultColumnHeight)
            startListHeight.add(height.toFloat() - paddingBottom - getDateHeight(date.key) - columnLineMargin - defaultColumnHeight)
        }
    }

    private val paintLine by lazy {
        Paint().apply {
            color = columnColor
            strokeWidth = columnWidth
            style = Paint.Style.FILL
        }
    }

    private val paintDate by lazy {
        Paint().apply {
            color = dateColor
            style = Paint.Style.FILL
            textSize = columnTextSize
            textAlign = Paint.Align.LEFT
        }
    }

    private val paintValue by lazy {
        Paint().apply {
            color = columnColor
            style = Paint.Style.FILL
            textSize = columnTextSize
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawColumn(canvas)
    }

    private fun getDateWidth(text: String): Int {
        val bound = Rect()
        paintDate.getTextBounds(
            text,
            0,
            text.length,
            bound
        )
        return bound.width()
    }

    private fun getMaxValue() = dataMap.values.max()

    private fun getDateHeight(text: String): Int {
        val bound = Rect()
        paintDate.getTextBounds(
            text,
            0,
            text.length,
            bound
        )
        return bound.height()
    }

    private fun getColumnHeight(value: Int): Float {
        val maxValue = getMaxValue()
        return if (value == maxValue) {
            maxColumnHeight
        } else {
            maxColumnHeight * value / maxValue
        }
    }

    private fun getDatesWidth(): Int {
        var width = 0
        if (dataMap.isNotEmpty()) {
            dataMap.forEach { (key, value) ->
                width += getDateWidth(key)
            }
        }
        return width
    }

    private fun drawColumn(canvas: Canvas) {
        var left = 0F
        var index = 0

        dataMap.forEach {
            canvas.drawText(
                it.key,
                left + indentText,
                height.toFloat() - paddingBottom,
                paintDate
            )

            endListHeight.add(
                height.toFloat() - paddingBottom - getDateHeight(it.key) - columnLineMargin - getColumnHeight(
                    it.value
                )
            )

            canvas.drawRoundRect(
                indentText + left + getDateWidth(it.key) / 2,
                currentListHeight[index],
                indentText + left + getDateWidth(it.key) / 2 + columnWidth,
                height.toFloat() - paddingBottom - getDateHeight(it.key) - columnLineMargin,
                columnCornerRadius,
                columnCornerRadius,
                paintLine
            )

            canvas.drawText(
                it.value.toString(),
                indentText + left + getDateWidth(it.key) / 2 + columnWidth / 2,
                currentListHeight[index] - columnLineMargin,
                paintValue
            )

            left += indentText + getDateWidth(it.key)
            index++
        }
    }

    fun startAnimation() {
        repeat(currentListHeight.size) { index ->
            val valueAnimator =
                ValueAnimator.ofFloat(startListHeight[index], endListHeight[index]).apply {
                    duration = (250 + (index + 1) * 100).toLong()
                    addUpdateListener {
                        val animatedHeight = it.animatedValue as Float
                        currentListHeight[index] = animatedHeight
                        invalidate()
                    }
                }
            valueAnimator.start()
        }
    }

    override fun onClick(v: View?) {
        startAnimation()
    }
}