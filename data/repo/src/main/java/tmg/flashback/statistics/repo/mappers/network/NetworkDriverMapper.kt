package tmg.flashback.statistics.repo.mappers.network

import tmg.flashback.statistics.network.models.drivers.DriverHistoryStanding
import tmg.flashback.statistics.network.models.drivers.DriverHistoryStandingRace
import tmg.flashback.statistics.room.models.drivers.DriverSeason
import tmg.flashback.statistics.room.models.drivers.DriverSeasonRace
import javax.inject.Inject

class NetworkDriverMapper @Inject constructor() {

    @Throws(RuntimeException::class)
    fun mapDriverSeason(driverId: String, driver: DriverHistoryStanding): DriverSeason {
        return DriverSeason(
            driverId = driverId,
            season = driver.season,
            championshipStanding = driver.championshipPosition,
            points = driver.points,
            inProgress = driver.inProgress
        )
    }

    @Throws(RuntimeException::class)
    fun mapDriverSeasonRace(driverId: String, season: Int, data: DriverHistoryStandingRace): DriverSeasonRace {
        return DriverSeasonRace(
            driverId = driverId,
            season = season,
            round = data.race.round,
            constructorId = data.construct.id,
            sprintQuali = data.sprintQuali ?: false,
            qualified = data.qualified,
            gridPos = data.gridPos,
            finished = data.finished,
            status = data.status,
            points = data.points,
        )
    }
}