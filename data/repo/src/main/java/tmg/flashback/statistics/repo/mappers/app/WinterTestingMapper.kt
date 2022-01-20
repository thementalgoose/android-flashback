package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.WinterTesting
import tmg.utilities.utils.LocalDateUtils.Companion.fromDate

class WinterTestingMapper {

    fun mapWinterTesting(model: tmg.flashback.statistics.room.models.race.WinterTesting): WinterTesting? {
        val date = fromDate(model.date) ?: return null
        return WinterTesting(
            label = model.label,
            date = date
        )
    }
}