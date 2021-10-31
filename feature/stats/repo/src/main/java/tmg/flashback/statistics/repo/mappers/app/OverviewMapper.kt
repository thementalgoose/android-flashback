package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.History
import tmg.flashback.formula1.model.HistoryRound
import tmg.flashback.statistics.room.models.overview.Overview
import tmg.utilities.extensions.toLocalDate
import tmg.utilities.extensions.toLocalTime
import java.lang.RuntimeException
import kotlin.jvm.Throws

class OverviewMapper {

    @Throws(RuntimeException::class)
    fun mapOverview(overview: Overview): HistoryRound {
        return HistoryRound(
            date = overview.date.toLocalDate()!!,
            time = overview.time?.toLocalTime(),
            season = overview.season,
            round = overview.round,
            raceName = overview.name,
            circuitId = overview.circuitId,
            circuitName = overview.circuitName,
            country = overview.country,
            countryISO = overview.countryISO,
            hasQualifying = overview.hasQualifying,
            hasResults = overview.hasRace
        )
    }
}