package tmg.flashback.data.repo.mappers.network

import tmg.flashback.data.persistence.models.overview.Event
import tmg.utilities.utils.LocalDateUtils.Companion.fromDate
import javax.inject.Inject

class NetworkEventMapper @Inject constructor() {

    fun mapEvent(season: Int, model: tmg.flashback.flashbackapi.api.models.overview.Event): Event? {
        fromDate(model.date) ?: return null
        return Event(
            season = season,
            label = model.label,
            type = model.type,
            date = model.date
        )
    }
}