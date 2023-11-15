package com.example.lesson_7_fokin.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.lesson_7_fokin.R
import com.example.lesson_7_fokin.data.model.Bridge
import com.example.lesson_7_fokin.data.model.BridgeCardListener
import com.example.lesson_7_fokin.data.model.StatusBridge
import com.example.lesson_7_fokin.databinding.CardBridgeBinding
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

import java.time.LocalTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

class BridgesViewHolder(
    private val parent: ViewGroup,
    private val bridgeCardListener: BridgeCardListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.card_bridge, null, false)
) {

    private val binding by viewBinding(CardBridgeBinding::bind)

    fun bind(item: Bridge) = with(binding) {
        root.setOnClickListener {
            bridgeCardListener.setOnClickCard(item)
        }
        textViewNameBridge.text = item.name

        item.divorces?.apply {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("H:mm")

            repeat(this.size) { it ->
                val currentTime = LocalTime.parse(LocalTime.now().format(timeFormatter))
                val startTime = LocalTime.parse(this[it].start as String, timeFormatter)
                val endTime = LocalTime.parse(this[it].end as String, timeFormatter)

                if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                    imageViewBridge.setImageResource(R.drawable.ic_brige_late)
                    item.status = StatusBridge.DIVORCED
                } else
                    if (lessOneHour(currentTime, startTime, endTime, formatter)) {
                        imageViewBridge.setImageResource(R.drawable.ic_brige_soon)
                        item.status = StatusBridge.SOON
                    } else {
                        imageViewBridge.setImageResource(R.drawable.ic_brige_normal)
                        item.status = StatusBridge.REDUCED
                    }
                if (it == 0) {
                    textViewTime1.text = root.resources.getString(
                        R.string.divorce,
                        this[it].start, this[it].end
                    )
                } else textViewTime2.text = root.resources.getString(R.string.divorce, this[it].start, this[it].end)
            }
        }
    }

    private fun lessOneHour(
        currentTime: LocalTime,
        startTime: LocalTime,
        endTime: LocalTime,
        formatter: DateTimeFormatter
    ): Boolean {

        val currentDateTime = currentTime.atDate(
            LocalDate.parse(
                LocalDate.now().format(formatter), formatter
            )
        )

        var startDateTime = LocalDateTime.now()

        if (currentTime.isBefore(startTime)) {
            startDateTime = startTime.atDate(
                LocalDate.parse(
                    LocalDate.now().format(formatter), formatter
                )
            )
        }
        if (currentTime.isAfter(endTime)) {
            startDateTime = startTime.atDate(
                LocalDate.parse(
                    LocalDate.now().plusDays(1).format(formatter), formatter
                )
            )
        }
        val period = Duration.between(currentDateTime, startDateTime)
        return period.toMinutes() <= 60
    }
}