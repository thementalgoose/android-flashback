package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.statistics.repo.mappers.app.ConstructorStandingMapper
import tmg.flashback.statistics.repo.mappers.app.DriverStandingMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class SeasonRepository(
    private val persistence: FlashbackDatabase,
    private val raceRepository: RaceRepository,
    private val driverStandingMapper: DriverStandingMapper,
    private val constructorStandingMapper: ConstructorStandingMapper
) {
    /**
     * races/{season}.json
     * Fetch race data for a specific season [season]
     * @param season
     */
    suspend fun fetchRaces(season: Int): Boolean {
        return raceRepository.fetchRaces(season)
    }

    fun getDriverStandings(driverId: String): Flow<SeasonDriverStandings?> {
        return persistence.seasonStandingDao().getDriverStandings(driverId)
            .map { standings ->
                driverStandingMapper.mapDriverStanding(standings)
            }
    }

    fun getConstructorStandings(constructorId: String): Flow<SeasonConstructorStandings?> {
        return persistence.seasonStandingDao().getConstructorStandings(constructorId)
            .map { standings ->
                constructorStandingMapper.mapConstructorStanding(standings)
            }
    }
}