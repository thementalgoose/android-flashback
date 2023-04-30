package tmg.flashback.domain.repo.mappers.network

import tmg.flashback.domain.persistence.models.overview.Event
import tmg.utilities.utils.LocalDateUtils.Companion.fromDate
import javax.inject.Inject

class NetworkEventMapper @Inject constructor() {

    fun mapEvent(season: Int, model: tmg.flashback.statistics.network.models.overview.Event): Event? {
        fromDate(model.date) ?: return null
        return Event(
            season = season,
            label = model.label,
            type = model.type,
            date = model.date
        )
    }
}