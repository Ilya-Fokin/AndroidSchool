package com.example.lesson_12_fokin.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.data.remote.model.ApiDivorce
import com.example.lesson_12_fokin.databinding.ViewBridgeRowBinding
import com.example.lesson_12_fokin.data.remote.model.Bridge

class BridgeRowView : ConstraintLayout {

    private val binding = ViewBridgeRowBinding.inflate(LayoutInflater.from(context), this)

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

    private fun init(context: Context, attributeSet: AttributeSet? = null) {
        val attrs = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.BridgeRowView,
            0,
            0
        )
        try {
            val color = attrs.getColor(
                R.styleable.BridgeRowView_bridgeTitleColor,
                ContextCompat.getColor(context, R.color.black)
            )
            val sizeTitle = attrs.getDimensionPixelSize(
                R.styleable.BridgeRowView_bridgeTitleSize,
                context.resources.getDimensionPixelSize(R.dimen.bridge_default_title_size)
            )
            val sizeTime = attrs.getDimensionPixelSize(
                R.styleable.BridgeRowView_bridgeTimeSize,
                context.resources.getDimensionPixelSize(R.dimen.bridge_default_time_size)
            )
            setTitleTextColor(color)
            setTitleSize(sizeTitle)
            setTimeSize(sizeTime)
        } finally {
            attrs.recycle()
        }
    }

    fun bind(bridge: Bridge) = with(binding) {
        imageViewBridge.setImageResource(bridge.iconId)
        textViewNameBridge.text = bridge.name
        textViewTime.text = setStringDivorces(bridge.divorces)
    }

    private fun setTitleTextColor(@ColorInt color: Int) {
        binding.textViewNameBridge.setTextColor(color)
    }

    private fun setTitleSize(dimen: Int) {
        binding.textViewNameBridge.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen.toFloat())
    }

    private fun setTimeSize(dimen: Int) {
        binding.textViewTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimen.toFloat())
    }

    private fun setStringDivorces(divorces: List<ApiDivorce>?): String {
        val times = StringBuilder()
        if (divorces?.isNotEmpty() == true) {
            repeat(divorces.size) { index ->
                val startTimeStr = divorces[index].start.orEmpty()
                times.append("$startTimeStr-")
                val endTimeStr = divorces[index].end.orEmpty()
                times.append("$endTimeStr ")
            }
        }
        return times.toString()
    }
}