package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Race
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.models.constructors.ConstructorData
import tmg.flashback.statistics.network.models.constructors.ConstructorStanding
import tmg.flashback.statistics.network.models.drivers.DriverData
import tmg.flashback.statistics.network.models.races.ConstructorStandings
import tmg.flashback.statistics.network.models.races.DriverStandings
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.extensions.valueList
import tmg.flashback.statistics.repo.mappers.app.ConstructorDataMapper
import tmg.flashback.statistics.repo.mappers.app.OverviewMapper
import tmg.flashback.statistics.repo.mappers.app.RaceMapper
import tmg.flashback.statistics.repo.mappers.network.*
import tmg.flashback.statistics.room.FlashbackDatabase

class RaceRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val networkConstructorDataMapper: NetworkConstructorDataMapper,
    private val networkDriverDataMapper: NetworkDriverDataMapper,
    private val networkRaceDataMapper: NetworkRaceDataMapper,
    private val networkRaceMapper: NetworkRaceMapper,
    private val networkDriverStandingMapper: NetworkDriverStandingMapper,
    private val networkConstructorStandingMapper: NetworkConstructorStandingMapper,
    private val raceMapper: RaceMapper
): BaseRepository(crashController) {

    /**
     * races/{season}.json
     * Fetch race data for a specific season [season]
     * @param season
     */
    suspend fun fetchRaces(season: Int): Boolean = attempt(
        apiCall = suspend { api.getSeason(season) },
        msgIfFailed = "races/${season}.json"
    ) { data ->

        saveConstructors(data.constructors)
        saveDrivers(data.drivers)

        saveConstructorStandings(season, data.constructorStandings)
        saveDriverStandings(season, data.driverStandings)

        val raceData = data.races.valueList().map { networkRaceDataMapper.mapRaceData(it.data) }
        val qualifyingResults = data.races.valueList()
            .map { race -> race.qualifying.values.map { Pair(race.data, it) } }
            .flatten()
            .map { (raceData, race) -> networkRaceMapper.mapQualifyingResults(raceData.season, raceData.round, race) }
        val raceResults = data.races.valueList()
            .map { race -> race.race.values.map { Pair(race.data, it) } }
            .flatten()
            .map { (raceData, race) -> networkRaceMapper.mapRaceResults(raceData.season, raceData.round, race) }

        persistence.seasonDao().insertRaces(raceData, qualifyingResults, raceResults)

        return@attempt true
    }

    /**
     * races/{season}/{round}.json
     * Fetch race data for a specific season [season] and [round]
     * @param season
     * @param round
     */
    suspend fun fetchRaces(season: Int, round: Int) = attempt(
        apiCall = suspend { api.getSeason(season, round) },
        msgIfFailed = "races/${season}/${round}.json"
    ) { data ->

        saveConstructors(data.constructors)
        saveDrivers(data.drivers)

        val raceData = networkRaceDataMapper.mapRaceData(data.data)
        val qualifyingResults = data.qualifying
            .valueList()
            .map { networkRaceMapper.mapQualifyingResults(data.data.season, data.data.round, it) }
        val raceResults = data.race
            .valueList()
            .map { networkRaceMapper.mapRaceResults(data.data.season, data.data.round, it) }

        persistence.seasonDao().insertRace(raceData, qualifyingResults, raceResults)

        return@attempt true
    }


    fun getRace(season: Int, round: Int): Flow<Race?> {
        return persistence.seasonDao().getRace(season, round)
            .map { race ->
                raceMapper.mapRace(race)
            }
    }



    private fun saveConstructorStandings(season: Int, constructors: Map<String, ConstructorStandings>?) {
        if (constructors == null) return
        val standings = constructors.values
            .map { networkConstructorStandingMapper.mapConstructorStanding(season, it) }
        val driverStandings = constructors.values
            .map { networkConstructorStandingMapper.mapConstructorStandingDriver(season, it) }
            .flatten()

        persistence.seasonStandingDao().insertConstructorStandingDrivers(driverStandings)
        persistence.seasonStandingDao().insertConstructorStandings(standings)
    }

    private fun saveDriverStandings(season: Int, drivers: Map<String, DriverStandings>?) {
        if (drivers == null) return
        val standings = drivers.values
            .map { networkDriverStandingMapper.mapDriverStanding(season, it) }
        val driverStandings = drivers.values
            .map { networkDriverStandingMapper.mapDriverStandingConstructor(season, it) }
            .flatten()

        persistence.seasonStandingDao().insertDriverStandingConstructors(driverStandings)
        persistence.seasonStandingDao().insertDriverStandings(standings)
    }

    private fun saveConstructors(constructors: Map<String, ConstructorData>?) {
        if (constructors == null) return
        val items = constructors.values
            .map { networkConstructorDataMapper.mapConstructorData(it) }
        persistence.constructorDao().insertAll(items)
    }

    private fun saveDrivers(drivers: Map<String, DriverData>?) {
        val items = (drivers?.values ?: emptyList())
            .map { networkDriverDataMapper.mapDriverData(it) }
        persistence.driverDao().insertAll(items)
    }
}