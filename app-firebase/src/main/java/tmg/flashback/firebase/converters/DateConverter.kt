package tmg.flashback.firebase.converters

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

fun fromDate(dateString: String, ofPattern: String = "yyyy-MM-dd"): LocalDate {
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(ofPattern))
}

fun fromTime(timeString: String?, ofPattern: String = "HH:mm:ss'Z'"): LocalTime? {
    if (timeString == null || timeString.isEmpty()) { return null }
    return LocalTime.parse(timeString, DateTimeFormatter.ofPattern(ofPattern))
}