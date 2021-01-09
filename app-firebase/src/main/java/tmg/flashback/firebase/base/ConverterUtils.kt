package tmg.flashback.firebase.base

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.jvm.Throws

object ConverterUtils {

    /**
     * Attempt a date parsing
     * @throws DateTimeParseException
     */
    @Throws(DateTimeParseException::class)
    fun fromDate(dateString: String): LocalDate {
        val validPatterns = listOf(
                "yyyy-M-d",
                "yyyy-MM-dd"
        )
        return validPatterns
                .mapNotNull { pattern ->
                    try {
                        LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern))
                    } catch (e: RuntimeException) {
                        null
                    }
                }
                .firstOrNull() ?: throw DateTimeParseException("Failed to parse date string $dateString", "", 0)
    }
    @JvmName("fromDateNullable")
    fun fromDate(dateString: String?): LocalDate? {
        if (dateString == null) {
            return null
        }
        return try {
            return fromDate(dateString)
        } catch (e: Exception) {
            null
        }
    }
    fun isDateValid(dateString: String?): Boolean {
        return fromDate(dateString) != null
    }

    /**
     * Attempt a time parsing
     * @throws DateTimeParseException
     */
    @Throws(DateTimeParseException::class)
    fun fromTime(timeString: String): LocalTime {
        val validPatterns = listOf(
                "HH:mm:ss'Z'",
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
                .firstOrNull() ?: throw DateTimeParseException("Failed to parse time string $timeString", "", 0)
    }
    @JvmName("fromTimeNullable")
    fun fromTime(timeString: String?): LocalTime? {
        if (timeString == null) {
            return null
        }
        return try {
            return fromTime(timeString)
        } catch (e: Exception) {
            null
        }
    }
    fun isTimeValid(timeString: String?): Boolean {
        return fromDate(timeString) != null
    }

}