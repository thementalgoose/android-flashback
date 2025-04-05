package tmg.flashback.formula1.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class Timestamp(
    private val originalDate: LocalDate,
    private val originalTime: LocalTime,
    private val currentTimeZone: ZoneId = ZoneId.systemDefault(),
    private val utcInstant: Instant = originalDate.atTime(originalTime).toInstant(ZoneOffset.UTC)
) {
    val utcLocalDateTime: LocalDateTime
        get() = originalDate.atTime(originalTime)

    /**
     * Local date time object
     */
    val deviceLocalDateTime: LocalDateTime
        get() {
            val zone = utcInstant.atZone(currentTimeZone)
            return zone.toLocalDateTime()
        }
    /**
     * Is the timestamp considered in the past based on UTC?
     */
    val isInPast: Boolean
        get() {
            return deviceLocalDateTime < LocalDateTime.now()
        }

    /**
     * Is the timestamp considered in the past based on UTC?
     */
    fun isInPastRelativeToo(deltaSeconds: Long) = deviceLocalDateTime < LocalDateTime.now().plusSeconds(deltaSeconds)

    /**
     * Is the timestamp considered to be today
     */
    val isToday: Boolean
        get() {
            return deviceLocalDateTime.toLocalDate() == LocalDate.now()
        }

    /**
     * Get a string representation of the date
     */
    fun string(): String {
        return this.originalDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                (this.originalTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) ?: "00:00:00")
    }
}