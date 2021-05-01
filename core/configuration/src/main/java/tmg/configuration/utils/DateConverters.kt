package tmg.configuration.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.jvm.Throws

object DateConverters {

    /**
     * Attempt a date parsing
     * @throws DateTimeParseException
     */
    @Throws(DateTimeParseException::class)
    fun fromDateRequired(dateString: String): LocalDate {
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
            .firstOrNull() ?: throw DateTimeParseException("Failed to parse date string $dateString with no supported patterns", "", 0)
    }
    fun fromDate(dateString: String?): LocalDate? {
        if (dateString == null) {
            return null
        }
        return try {
            return fromDateRequired(dateString)
        } catch (e: Exception) {
            null
        }
    }
    fun isDateValid(dateString: String?): Boolean {
        return fromDate(dateString) != null
    }
}