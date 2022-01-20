package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.room.models.race.WinterTesting
import tmg.utilities.utils.LocalDateUtils.Companion.fromDate

class NetworkWinterTestingMapper {

    fun mapWinterTesting(season: Int, model: tmg.flashback.statistics.network.models.races.WinterTesting): WinterTesting? {
        fromDate(model.date) ?: return null
        return WinterTesting(
            season = season,
            label = model.label,
            date = model.date
        )
    }
}