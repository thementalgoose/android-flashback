package tmg.flashback.statistics.repo.mappers.app

import tmg.flashback.formula1.model.DriverHistory
import tmg.flashback.formula1.model.DriverHistorySeason
import tmg.flashback.formula1.model.DriverHistorySeasonRace
import tmg.flashback.statistics.room.models.drivers.DriverSeasonRaceWithConstructor
import tmg.flashback.statistics.room.models.drivers.DriverSeasonWithRaces
import javax.inject.Inject

class DriverMapper @Inject constructor(
    private val driverDataMapper: DriverDataMapper,
    private val constructorDataMapper: ConstructorDataMapper,
    private val raceDataMapper: RaceMapper
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
            raceInfo = raceDataMapper.mapRaceInfoWithCircuit(data.round),
            constructor = constructorDataMapper.mapConstructorData(data.constructor),
        )
    }
}