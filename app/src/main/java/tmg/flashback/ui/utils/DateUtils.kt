package tmg.flashback.ui.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.Period

/**
 * Calculate the age of someone
 */
fun LocalDate.age(now: LocalDate = LocalDate.now()): Int {
    return Period.between(this, now).years
}