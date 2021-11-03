package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.room.models.overview.Overview
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime
import java.lang.RuntimeException
import kotlin.jvm.Throws

class OverviewMapper {

    @Throws(RuntimeException::class)
    fun mapOverview(overview: Overview): OverviewRace {
        return OverviewRace(
            date = requireFromDate(overview.date),
            time = fromTime(overview.time),
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