package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import tmg.flashback.formula1.model.Season
import tmg.flashback.formula1.model.SeasonConstructorStandings
import tmg.flashback.formula1.model.SeasonDriverStandings
import tmg.flashback.statistics.repo.mappers.app.ConstructorStandingMapper
import tmg.flashback.statistics.repo.mappers.app.DriverStandingMapper
import tmg.flashback.statistics.repo.mappers.app.SeasonMapper
import tmg.flashback.statistics.room.FlashbackDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonRepository @Inject constructor(
    private val persistence: FlashbackDatabase,
    private val raceRepository: RaceRepository,
    private val driverStandingMapper: DriverStandingMapper,
    private val constructorStandingMapper: ConstructorStandingMapper,
    private val seasonMapper: SeasonMapper
) {
    /**
     * races/{season}.json
     * Fetch race data for a specific season [season]
     * @param season
     */
    suspend fun fetchRaces(season: Int): Boolean {
        return raceRepository.fetchRaces(season)
    }

    fun getSeason(season: Int): Flow<Season?> {
        return combine(
            persistence.seasonDao().getRaces(season),
            persistence.eventsDao().getEvents(season)
        ) { list, winterTesting -> Pair(list, winterTesting) }
            .map { (list, winterTesting) ->
                seasonMapper.mapSeason(season, list, winterTesting)
            }
    }

    fun getDriverStandings(season: Int): Flow<SeasonDriverStandings?> {
        return persistence.seasonStandingDao().getDriverStandings(season)
            .map { standings ->
                driverStandingMapper.mapDriverStanding(standings)
            }
    }

    fun getConstructorStandings(season: Int): Flow<SeasonConstructorStandings?> {
        return persistence.seasonStandingDao().getConstructorStandings(season)
            .map { standings ->
                constructorStandingMapper.mapConstructorStanding(standings)
            }
    }
}