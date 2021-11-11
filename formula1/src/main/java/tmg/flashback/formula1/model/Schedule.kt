package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class Schedule(
    val label: String,
    val date: LocalDate,
    val time: LocalTime
) {
    companion object
}