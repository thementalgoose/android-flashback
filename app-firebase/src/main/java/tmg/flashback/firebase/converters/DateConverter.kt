package tmg.flashback.firebase.converters

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException

fun fromDate(dateString: String, ofPattern: String = "yyyy-M-d"): LocalDate {
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(ofPattern))
}

fun fromTime(timeString: String?, ofPattern: String = "HH:mm:ss'Z'"): LocalTime? {
    if (timeString == null || timeString.isEmpty()) { return null }
    return try {
        LocalTime.parse(timeString, DateTimeFormatter.ofPattern(ofPattern))
    } catch (e: DateTimeParseException) {
        null
    }
}