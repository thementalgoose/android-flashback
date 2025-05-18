package tmg.flashback.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.data.persistence.FlashbackDatabase
import tmg.flashback.data.repo.base.BaseRepository
import tmg.flashback.data.repo.extensions.valueList
import tmg.flashback.data.repo.mappers.app.RaceMapper
import tmg.flashback.data.repo.mappers.network.NetworkConstructorDataMapper
import tmg.flashback.data.repo.mappers.network.NetworkConstructorStandingMapper
import tmg.flashback.data.repo.mappers.network.NetworkDriverDataMapper
import tmg.flashback.data.repo.mappers.network.NetworkDriverStandingMapper
import tmg.flashback.data.repo.mappers.network.NetworkRaceDataMapper
import tmg.flashback.data.repo.mappers.network.NetworkRaceMapper
import tmg.flashback.data.repo.mappers.network.NetworkScheduleMapper
import tmg.flashback.data.repo.repository.CacheRepository
import tmg.flashback.flashbackapi.api.api.FlashbackApi
import tmg.flashback.flashbackapi.api.models.constructors.Constructor
import tmg.flashback.flashbackapi.api.models.drivers.Driver
import tmg.flashback.flashbackapi.api.models.races.ConstructorStandings
import tmg.flashback.flashbackapi.api.models.races.DriverStandings
import tmg.flashback.formula1.model.Race
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RaceRepository @Inject constructor(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashlyticsManager,
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
        val raceResults = data.races.valueList()
            .map { race -> race.race?.values?.map { Pair(race.data, it) } ?: emptyList() }
            .flatten()
            .map { (raceData, race) -> networkRaceMapper.mapRaceResults(raceData.season, raceData.round, race) }

        val sprintQualifyingResults = data.races.valueList()
            .map { race -> race.sprintEvent?.qualifying?.values?.map { Pair(race.data, it) } ?: emptyList() }
            .flatten()
            .map { (raceData, sprint) -> networkRaceMapper.mapSprintQualifyingResult(raceData.season, raceData.round, sprint)}
        val sprintRaceResults = data.races.valueList()
            .map { race -> race.sprintEvent?.race?.values?.map { Pair(race.data, it) } ?: emptyList() }
            .flatten()
            .map { (raceData, sprint) -> networkRaceMapper.mapSprintRaceResults(raceData.season, raceData.round, sprint)}

        val schedules = data.races.valueList()
            .map { race -> networkScheduleMapper.mapSchedules(race) }
            .flatten()

        persistence.seasonDao().insertRaces(raceData, qualifyingResults, raceResults, sprintQualifyingResults, sprintRaceResults)
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
        val raceResults = data.race
            .valueList()
            .map { networkRaceMapper.mapRaceResults(data.data.season, data.data.round, it) }
        val sprintQualifyingResults = data.sprintEvent?.qualifying
            .valueList()
            .map { networkRaceMapper.mapSprintQualifyingResult(data.data.season, data.data.round, it) }
        val sprintRaceResults = data.sprintEvent?.race
            .valueList()
            .map { networkRaceMapper.mapSprintRaceResults(data.data.season, data.data.round, it) }
        val schedules = data.schedule
            ?.map { schedule -> networkScheduleMapper.mapSchedule(data.data.season, data.data.round, schedule) } ?: emptyList()

        persistence.scheduleDao().replaceAllForRace(data.data.season, data.data.round, schedules)
        persistence.seasonDao().insertRace(raceData, qualifyingResults, raceResults, sprintQualifyingResults, sprintRaceResults)

        return@attempt true
    }

    fun getRace(season: Int, round: Int): Flow<Race?> {
        return persistence.seasonDao().getRace(season, round)
            .map { race ->
                raceMapper.mapRace(race)
            }
    }

    fun hasPreviouslySynced(season: Int): Boolean {
        return cacheRepository.seasonsSyncAtLeastOnce.contains(season)
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