package tmg.flashback.formula1.model

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.EventType

data class Event(
    val label: String,
    val type: EventType,
    val date: LocalDate
) {
    companion object
}