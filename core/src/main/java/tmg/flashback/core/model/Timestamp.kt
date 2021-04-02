package tmg.flashback.core.model

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

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
     * Get the best guess for the date that this item should apply too
     *  - It will use original date in a lot of circumstances but if it has a time,
     *    and that time with the timezone means the date falls on another day then
     *    show it on that day
     */
    val deviceDate: LocalDate
        get() = deviceLocalDateTime?.toLocalDate() ?: originalDate

    /**
     * UTC instant that is provided. Only not null if both date and time are provided
     */
    private var _utcInstant: Instant? = null

    /**
     * Local date time object
     */
    private val deviceLocalDateTime: LocalDateTime?
        get() {
            val zone = _utcInstant?.atZone(zone)
            return zone?.toLocalDateTime()
        }

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
        deviceLocalDateTime?.let {
            if (!isDateOnly) {
                callback(originalDate.atTime(originalTime), it)
            }
        }
    }

    /**
     * Run the [callback] block if there is only a date set
     * @param callback Callback containing only the date
     */
    fun ifDate(callback: (date: LocalDate) -> Unit) {
        if (isDateOnly || deviceLocalDateTime == null) {
            callback(originalDate)
        }
    }

    /**
     * Is the timestamp considered in the past based on UTC?
     */
    val isInPast: Boolean
        get() {
            return if (deviceLocalDateTime != null) {
                deviceLocalDateTime!! < LocalDateTime.now()
            } else {
                println("Date ${LocalDate.now()} - ${originalDate}")
                originalDate < LocalDate.now()
            }
        }

    /**
     * Get a string representation of the date
     */
    fun string(): String {
        return this.originalDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                (this.originalTime?.format(DateTimeFormatter.ofPattern("HH:mm:ss")) ?: "00:00:00")
    }
}