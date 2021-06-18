package tmg.flashback.upnext.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.ChronoUnit

/**
 * Days between start and end
 */
fun daysBetween(start: LocalDate, end: LocalDate): Int {
    return when {
        start < end -> ChronoUnit.DAYS.between(start, end).toInt()
        start > end -> -ChronoUnit.DAYS.between(end, start).toInt()
        else -> 0
    }
}

/**
 * Seconds between to local time values
 */
fun secondsBetween(start: LocalTime, end: LocalTime): Int {
    return when {
        start < end -> ChronoUnit.SECONDS.between(start, end).toInt()
        start > end -> -ChronoUnit.SECONDS.between(end, start).toInt()
        else -> 0
    }
}

