package com.example.lesson_7_fokin.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lesson_7_fokin.R
import com.example.lesson_7_fokin.data.model.Bridge
import com.example.lesson_7_fokin.data.model.BridgeCardListener
import com.example.lesson_7_fokin.data.model.StatusBridge
import com.example.lesson_7_fokin.databinding.ItemBridgeBinding
import java.lang.StringBuilder
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime

import java.time.LocalTime
import java.time.format.DateTimeFormatter

class BridgesViewHolder(
    private val parent: ViewGroup,
    private val bridgeCardListener: BridgeCardListener
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_bridge, parent, false)
) {

    private val binding by viewBinding(ItemBridgeBinding::bind)

    fun bind(item: Bridge) = with(binding) {
        root.setOnClickListener {
            bridgeCardListener.setOnClickCard(item)
        }
        textViewNameBridge.text = item.name

        val times = StringBuilder()

        item.divorces?.apply {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

            repeat(this.size) { it ->
                val currentTimeStr = LocalTime.now().toString()
                val startTimeStr = this[it].start as String
                times.append("$startTimeStr-")
                val endTimeStr = this[it].end as String
                times.append("$endTimeStr ")

                val currentTime = LocalTime.parse(LocalTime.now().format(changeFormatTime(currentTimeStr)))
                val startTime = LocalTime.parse(this[it].start as String, changeFormatTime(startTimeStr))
                val endTime = LocalTime.parse(this[it].end as String, changeFormatTime(endTimeStr))

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
            }
        }
        binding.textViewTime.text = times
    }

    private fun changeFormatTime(time: String) : DateTimeFormatter {
        val timeFormatter1 = DateTimeFormatter.ofPattern("H:mm")
        val timeFormatter2 = DateTimeFormatter.ofPattern("HH:mm")
        val arr = time.split(":")
        return if (arr[0][0] == '0') {
            timeFormatter2
        } else timeFormatter1
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