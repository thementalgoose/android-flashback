package tmg.flashback.data.repo.mappers.app

import tmg.flashback.data.persistence.models.overview.OverviewWithCircuit
import tmg.flashback.formula1.model.OverviewRace
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate
import tmg.utilities.utils.LocalTimeUtils.Companion.fromTime
import javax.inject.Inject

class OverviewMapper @Inject constructor(
    private val scheduleMapper: ScheduleMapper
) {

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
            laps = overview.overview.laps,
            countryISO = overview.circuit.countryISO,
            hasQualifying = overview.overview.hasQualifying,
            hasSprint = overview.overview.hasSprint,
            hasResults = overview.overview.hasRace,
            schedule = overview.schedule.mapNotNull { scheduleMapper.mapSchedule(it) }
        )
    }
}