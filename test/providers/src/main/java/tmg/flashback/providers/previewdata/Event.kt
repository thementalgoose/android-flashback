package tmg.flashback.providers.previewdata

import org.threeten.bp.LocalDate
import tmg.flashback.formula1.enums.EventType
import tmg.flashback.formula1.model.Event

internal fun Event.Companion.model(
    label: String = "label",
    type: EventType = EventType.TESTING,
    date: LocalDate = LocalDate.of(2020, 10, 12)
): Event = Event(
    label = label,
    type = type,
    date = date
)