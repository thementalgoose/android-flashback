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
     * Method to be called on timestamp object to perform certain actions depending on
     *   the data supplied by the API
     *
     * If only a date is returned, then return the best guess date that was supplied.
     *   Device / Instant metrics should not take affect
     *
     * If time is available, return both the UTC raw value as well as the device localdatetime
     */
    fun perform(
            ifDateOnly: (date: LocalDate) -> Unit,
            ifTime: (utc: LocalDateTime, device: LocalDateTime) -> Unit
    ) {
        if (!isDateOnly && _utcInstant != null) {
            val zone = _utcInstant!!.atZone(zone)
            ifTime(originalDate.atTime(originalTime), zone.toLocalDateTime())
        }
        else {
            ifDateOnly(originalDate)
        }
    }

}