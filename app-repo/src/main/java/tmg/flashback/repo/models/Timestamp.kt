package tmg.flashback.repo.models

import org.threeten.bp.*

data class Timestamp(
        val originalDate: LocalDate,
        val originalTime: LocalTime? = null,
        private val zone: ZoneId = ZoneId.systemDefault()
) {
    /**
     * Determine if the date provided was a date only
     */
    val isDateOnly: Boolean
        get() = originalTime == null

    /**
     * UTC instant that is provided. Only not null if both date and time are provided
     */
    private var _utcInstant: Instant? = null

    init {
        if (originalTime != null) {
            _utcInstant = originalDate.atTime(originalTime).toInstant(ZoneOffset.UTC)
        }
    }

    /**
     * Run the [callback] block if there is a date and a time set
     * @param callback Callback containing utc time and local device time
     */
    fun ifDateAndTime(callback: (utc: LocalDateTime, local: LocalDateTime) -> Unit) {
        if (!isDateOnly && _utcInstant != null) {
            val zone = _utcInstant!!.atZone(zone)
            callback(originalDate.atTime(originalTime), zone.toLocalDateTime())
        }
    }

    /**
     * Run the [callback] block if there is only a date set
     * @param callback Callback containing only the date
     */
    fun ifDate(callback: (date: LocalDate) -> Unit) {
        if (isDateOnly || _utcInstant == null) {
            callback(originalDate)
        }
    }
}