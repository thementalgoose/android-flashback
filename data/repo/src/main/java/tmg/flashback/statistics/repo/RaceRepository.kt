package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.formula1.model.Race
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.models.constructors.Constructor
import tmg.flashback.statistics.network.models.drivers.Driver
import tmg.flashback.statistics.network.models.races.ConstructorStandings
import tmg.flashback.statistics.network.models.races.DriverStandings
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.extensions.valueList
import tmg.flashback.statistics.repo.mappers.app.RaceMapper
import tmg.flashback.statistics.repo.mappers.app.EventMapper
import tmg.flashback.statistics.repo.mappers.network.*
import tmg.flashback.statistics.repo.repository.CacheRepository
import tmg.flashback.statistics.room.FlashbackDatabase

class RaceRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    networkConnectivityManager: NetworkConnectivityManager,
    private val networkConstructorDataMapper: NetworkConstructorDataMapper,
    private val networkDriverDataMapper: NetworkDriverDataMapper,
    private val networkRaceDataMapper: NetworkRaceDataMapper,
    private val networkRaceMapper: NetworkRaceMapper,
    private val networkDriverStandingMapper: NetworkDriverStandingMapper,
    private val networkConstructorStandingMapper: NetworkConstructorStandingMapper,
    private val networkScheduleMapper: NetworkScheduleMapper,
    private val raceMapper: RaceMapper,
    private val cacheRepository: CacheRepository
): BaseRepository(crashController, networkConnectivityManager) {

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
            .map { race -> race.qualifying?.values?.map { Pair(race.data, it) } ?: emptyList() }
            .flatten()
            .map { (raceData, qualifying) -> networkRaceMapper.mapQualifyingResults(raceData.season, raceData.round, qualifying) }
        val sprintResults = data.races.valueList()
            .map { race -> race.sprint?.values?.map { Pair(race.data, it) } ?: emptyList() }
            .flatten()
            .map { (raceData, sprint) -> networkRaceMapper.mapSprintResults(raceData.season, raceData.round, sprint)}
        val raceResults = data.races.valueList()
            .map { race -> race.race?.values?.map { Pair(race.data, it) } ?: emptyList() }
            .flatten()
            .map { (raceData, race) -> networkRaceMapper.mapRaceResults(raceData.season, raceData.round, race) }
        val schedules = data.races.valueList()
            .map { race -> networkScheduleMapper.mapSchedules(race) }
            .flatten()

        persistence.seasonDao().insertRaces(raceData, qualifyingResults, sprintResults, raceResults)
        persistence.scheduleDao().replaceAllForSeason(season, schedules)

        val set = cacheRepository.seasonsSyncAtLeastOnce.toMutableSet()
        set.add(season)
        cacheRepository.seasonsSyncAtLeastOnce = set

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
        val sprintResults = data.sprint
            .valueList()
            .map { networkRaceMapper.mapSprintResults(data.data.season, data.data.round, it) }
        val raceResults = data.race
            .valueList()
            .map { networkRaceMapper.mapRaceResults(data.data.season, data.data.round, it) }
        val schedules = data.schedule
            ?.map { schedule -> networkScheduleMapper.mapSchedule(data.data.season, data.data.round, schedule) } ?: emptyList()

        persistence.scheduleDao().replaceAllForRace(data.data.season, data.data.round, schedules)
        persistence.seasonDao().insertRace(raceData, qualifyingResults, sprintResults, raceResults)

        return@attempt true
    }

    fun getRace(season: Int, round: Int): Flow<Race?> {
        return persistence.seasonDao().getRace(season, round)
            .map { race ->
                raceMapper.mapRace(race)
            }
    }

    suspend fun shouldSyncRace(season: Int): Boolean {
        return !cacheRepository.seasonsSyncAtLeastOnce.contains(season)
    }
    suspend fun shouldSyncRace(season: Int, @Suppress("UNUSED_PARAMETER") round: Int): Boolean {
        return shouldSyncRace(season)
    }




    private fun saveConstructorStandings(season: Int, constructors: Map<String, ConstructorStandings>?) {
        if (constructors == null) return
        val standings = constructors.values
            .map { networkConstructorStandingMapper.mapConstructorStanding(season, it) }
        val driverStandings = constructors.values
            .map { networkConstructorStandingMapper.mapConstructorStandingDriver(season, it) }
            .flatten()

        persistence.seasonStandingDao().insertConstructorStandings(standings, driverStandings)
    }

    private fun saveDriverStandings(season: Int, drivers: Map<String, DriverStandings>?) {
        if (drivers == null) return
        val standings = drivers.values
            .map { networkDriverStandingMapper.mapDriverStanding(season, it) }
        val driverStandings = drivers.values
            .map { networkDriverStandingMapper.mapDriverStandingConstructor(season, it) }
            .flatten()

        persistence.seasonStandingDao().insertDriverStandings(standings, driverStandings)
    }

    private fun saveConstructors(constructors: Map<String, Constructor>?) {
        if (constructors == null) return
        val items = constructors.values
            .map { networkConstructorDataMapper.mapConstructorData(it) }
        persistence.constructorDao().insertAll(items)
    }

    private fun saveDrivers(drivers: Map<String, Driver>?) {
        val items = (drivers?.values ?: emptyList())
            .map { networkDriverDataMapper.mapDriverData(it) }
        persistence.driverDao().insertAll(items)
    }
}