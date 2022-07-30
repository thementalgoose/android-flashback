package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.enums.EventType
import tmg.flashback.formula1.model.Event
import tmg.utilities.extensions.toEnum
import tmg.utilities.utils.LocalDateUtils.Companion.fromDate
import javax.inject.Inject

class EventMapper @Inject constructor() {

    fun mapEvent(model: tmg.flashback.statistics.room.models.overview.Event): Event? {
        val date = fromDate(model.date) ?: return null
        return Event(
            label = model.label,
            type = model.type.toEnum<EventType> { it.key } ?: EventType.OTHER,
            date = date
        )
    }
}