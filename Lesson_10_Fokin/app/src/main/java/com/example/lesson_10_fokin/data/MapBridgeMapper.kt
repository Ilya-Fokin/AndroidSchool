package com.example.lesson_10_fokin.data


import com.example.lesson_10_fokin.R
import com.example.lesson_10_fokin.data.model.Bridge
import com.example.lesson_10_fokin.data.model.Divorce
import com.example.lesson_10_fokin.data.model.MapBridge
import com.yandex.mapkit.geometry.Point
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object MapBridgeMapper {
    fun toBridge(bridges: List<Bridge>): MutableList<MapBridge> {
        val mapBridges: MutableList<MapBridge> = mutableListOf()
        repeat(bridges.size) { index ->
            mapBridges.add(
                MapBridge(
                    bridges[index].id,
                    bridges[index].name,
                    bridges[index].nameEng,
                    bridges[index].description,
                    bridges[index].descriptionEng,
                    bridges[index].divorces,
                    bridges[index].closedUrl,
                    bridges[index].openUrl,
                    bridges[index].public,
                    Point(bridges[index].lat as Double, bridges[index].lng as Double),
                    setIcon(bridges[index].divorces)
                )
            )
        }
        return mapBridges
    }

    private fun setIcon(divorces: List<Divorce>?): Int {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var iconId: Int = 0

        if (divorces != null) {
            repeat(divorces.size) { it ->
                val currentTimeStr = LocalTime.now().toString()
                val startTimeStr = divorces[it].start.orEmpty()
                val endTimeStr = divorces[it].end.orEmpty()

                val currentTime =
                    LocalTime.parse(LocalTime.now().format(changeFormatTime(currentTimeStr)))
                val startTime =
                    LocalTime.parse(divorces[it].start.orEmpty(), changeFormatTime(startTimeStr))
                val endTime =
                    LocalTime.parse(divorces[it].end.orEmpty(), changeFormatTime(endTimeStr))

                iconId = if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                    R.drawable.ic_bridge_late
                } else
                    if (lessOneHour(currentTime, startTime, endTime, formatter)) {
                        R.drawable.ic_bridge_soon
                    } else {
                        R.drawable.ic_bridge_normal
                    }
            }
        }
        return iconId
    }

    private fun changeFormatTime(time: String): DateTimeFormatter {
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