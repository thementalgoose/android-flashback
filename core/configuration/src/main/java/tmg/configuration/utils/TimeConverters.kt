package tmg.configuration.utils

import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import tmg.configuration.utils.DateConverters.fromDate
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.jvm.Throws

object TimeConverters {

    /**
     * Attempt a time parsing
     * @throws DateTimeParseException
     */
    @Throws(DateTimeParseException::class)
    fun fromTimeRequired(timeString: String): LocalTime {
        val validPatterns = listOf(
            "HH:mm:ss'Z'",
            "HH:mm:ssZ",
            "HH:mm:ss",
            "HH:mm"
        )
        return validPatterns
            .mapNotNull { pattern ->
                try {
                    LocalTime.parse(timeString, DateTimeFormatter.ofPattern(pattern))
                } catch (e: RuntimeException) {
                    null
                }
            }
            .firstOrNull() ?: throw DateTimeParseException("Failed to parse time string $timeString with no supported patterns.", "", 0)
    }
    fun fromTime(timeString: String?): LocalTime? {
        if (timeString == null) {
            return null
        }
        return try {
            return fromTimeRequired(timeString)
        } catch (e: Exception) {
            null
        }
    }
    fun isTimeValid(timeString: String?): Boolean {
        return fromDate(timeString) != null
    }
}