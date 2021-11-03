package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.room.models.overview.Overview
import tmg.flashback.statistics.room.models.overview.OverviewWithCircuit
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime
import java.lang.RuntimeException
import kotlin.jvm.Throws

class OverviewMapper {

    @Throws(RuntimeException::class)
    fun mapOverview(overview: OverviewWithCircuit): OverviewRace {
        return OverviewRace(
            date = requireFromDate(overview.overview.date),
            time = fromTime(overview.overview.time),
            season = overview.overview.season,
            round = overview.overview.round,
            raceName = overview.overview.name,
            circuitId = overview.overview.circuitId,
            circuitName = overview.circuit.name,
            country = overview.circuit.country,
            countryISO = overview.circuit.countryISO,
            hasQualifying = overview.overview.hasQualifying,
            hasResults = overview.overview.hasRace
        )
    }
}