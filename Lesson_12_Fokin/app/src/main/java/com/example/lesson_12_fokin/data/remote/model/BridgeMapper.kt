package com.example.lesson_12_fokin.data.remote.model

import com.example.lesson_12_fokin.R
import com.example.lesson_12_fokin.presentation.bridges.Bridge
import com.example.lesson_12_fokin.presentation.bridges.StatusBridge
import com.yandex.mapkit.geometry.Point
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object BridgeMapper {

    /** Маппер для правильного моста из API */
    fun toBridge(bridge: ApiBridge): Bridge {
        val status = setCurrentStatus(bridge.divorces)
        return Bridge(
            bridge.id ?: -1,
            bridge.name.orEmpty(),
            bridge.nameEng.orEmpty(),
            bridge.description.orEmpty(),
            bridge.descriptionEng.orEmpty(),
            bridge.divorces.orEmpty(),
            status,
            setCurrentImgUrl(bridge, status),
            bridge.public ?: false,
            Point(bridge.lat as Double, bridge.lng as Double),
            setIcon(status)
        )
    }

    /** Маппер для моста, полученного без времени развода из API */
    fun toBridgeWithoutDivorces(bridge: ApiBridge, divorces: List<Divorce>): Bridge {
        val status = setCurrentStatus(divorces)
        return Bridge(
            bridge.id ?: -1,
            bridge.name.orEmpty(),
            bridge.nameEng.orEmpty(),
            bridge.description.orEmpty(),
            bridge.descriptionEng.orEmpty(),
            divorces,
            status,
            setCurrentImgUrl(bridge, status),
            bridge.public ?: false,
            Point(bridge.lat as Double, bridge.lng as Double),
            setIcon(status)
        )
    }

    private fun setIcon(status: StatusBridge): Int {
        return when (status) {
            StatusBridge.DIVORCED -> R.drawable.ic_bridge_late
            StatusBridge.SOON -> R.drawable.ic_bridge_soon
            StatusBridge.REDUCED -> R.drawable.ic_bridge_normal
        }
    }

    private fun setCurrentStatus(divorces: List<Divorce>?): StatusBridge {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var status: StatusBridge = StatusBridge.DIVORCED

        if (divorces != null) {
            repeat(divorces.size) {
                val currentTimeStr = LocalTime.now().toString()
                val startTimeStr = divorces[it].start.orEmpty()
                val endTimeStr = divorces[it].end.orEmpty()

                val currentTime =
                    LocalTime.parse(LocalTime.now().format(changeFormatTime(currentTimeStr)))
                val startTime =
                    LocalTime.parse(divorces[it].start.orEmpty(), changeFormatTime(startTimeStr))
                val endTime =
                    LocalTime.parse(divorces[it].end.orEmpty(), changeFormatTime(endTimeStr))

                status = if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                    StatusBridge.DIVORCED
                } else
                    if (lessOneHour(currentTime, startTime, endTime, formatter)) {
                        StatusBridge.SOON
                    } else {
                        StatusBridge.REDUCED
                    }
            }
        }
        return status
    }

    /** Выбирается нужный паттерн в зависимости от полученного времени */
    private fun changeFormatTime(time: String): DateTimeFormatter {
        val timeFormatter1 = DateTimeFormatter.ofPattern("H:mm")
        val timeFormatter2 = DateTimeFormatter.ofPattern("HH:mm")
        val arr = time.split(":")
        return if (arr[0][0] == '0') {
            timeFormatter2
        } else timeFormatter1
    }

    /** Определяет, осталось ли меньше час до развода моста */
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

    private fun setCurrentImgUrl(bridge: ApiBridge, status: StatusBridge): String {
        return when (status) {
            StatusBridge.DIVORCED -> bridge.openUrl.orEmpty()
            StatusBridge.SOON -> bridge.closedUrl.orEmpty()
            StatusBridge.REDUCED -> bridge.closedUrl.orEmpty()
        }
    }
}