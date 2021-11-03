package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeasonRace
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.statistics.room.models.drivers.DriverSeasonRaceWithConstructor
import tmg.flashback.statistics.room.models.drivers.DriverSeasonWithRaces
import tmg.utilities.utils.LocalDateUtils.Companion.requireFromDate

class DriverMapper(
    private val driverDataMapper: DriverDataMapper,
    private val constructorDataMapper: ConstructorDataMapper
) {

    fun mapDriver(history: tmg.flashback.statistics.room.models.drivers.DriverHistory?): DriverHistory? {
        if (history == null) return null
        return DriverHistory(
            driver = driverDataMapper.mapDriver(history.driver),
            standings = history.seasons.map {
                mapDriverSeasonWithRaces(it)
            }
        )
    }

    private fun mapDriverSeasonWithRaces(data: DriverSeasonWithRaces): DriverHistorySeason {
        return DriverHistorySeason(
            championshipStanding = data.driverSeason.championshipStanding,
            isInProgress = data.driverSeason.inProgress,
            points = data.driverSeason.points,
            season = data.driverSeason.season,
            constructors = data.races
                .map { it.constructor }
                .toSet()
                .map { constructorDataMapper.mapConstructorData(it) },
            raceOverview = data.races.map { mapDriverSeasonRace(it) }
        )
    }

    private fun mapDriverSeasonRace(data: DriverSeasonRaceWithConstructor): DriverHistorySeasonRace {
        return DriverHistorySeasonRace(
            status = data.race.status,
            finished = data.race.finished,
            points = data.race.points,
            qualified = data.race.qualified,
            gridPos = data.race.gridPos,
            round = data.race.round,
            season = data.race.season,
            raceName = data.round.raceInfo.name,
            date = requireFromDate(data.round.raceInfo.date),
            constructor = constructorDataMapper.mapConstructorData(data.constructor),
            circuitName = data.round.circuit.name,
            circuitId = data.round.circuit.id,
            circuitNationality = data.round.circuit.country,
            circuitNationalityISO = data.round.circuit.countryISO
        )
    }


}