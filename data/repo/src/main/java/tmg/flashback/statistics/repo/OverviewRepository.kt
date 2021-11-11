package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.OverviewMapper
import tmg.flashback.statistics.repo.mappers.app.ScheduleMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkCircuitDataMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkOverviewMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkScheduleMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class OverviewRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val overviewMapper: OverviewMapper,
    private val networkOverviewMapper: NetworkOverviewMapper,
    private val networkCircuitDataMapper: NetworkCircuitDataMapper,
    private val networkScheduleMapper: NetworkScheduleMapper
): BaseRepository(crashController) {

    /**
     * overview.json
     * Fetch overview data for all seasons currently available
     */
    suspend fun fetchOverview(): Boolean = attempt(
        apiCall = suspend { api.getOverview() },
        msgIfFailed = "overview.json"
    ) { data ->
        val allCircuits = data.values.map { networkCircuitDataMapper.mapCircuitData(it.circuit) }
        val allOverview = data.values
            .mapNotNull { networkOverviewMapper.mapOverview(it) }
        val scheduled = data.values
            .map { networkScheduleMapper.mapSchedules(it) }
            .flatten()

        persistence.circuitDao().insertCircuit(allCircuits)
        persistence.overviewDao().insertAll(allOverview)
        persistence.scheduleDao().insertAll(scheduled)

        return@attempt true
    }

    /**
     * overview/{season}.json
     * Fetch overview data for a specific season [season]
     * @param season
     */
    suspend fun fetchOverview(season: Int): Boolean = attempt(
        apiCall = suspend { api.getOverview(season) },
        msgIfFailed = "overview/${season}.json"
    ) { data ->
        val allCircuits = data.values.map { networkCircuitDataMapper.mapCircuitData(it.circuit) }
        val allOverview = data.values
            .mapNotNull { networkOverviewMapper.mapOverview(it) }
        val scheduled = data.values
            .map { networkScheduleMapper.mapSchedules(it) }
            .flatten()

        persistence.circuitDao().insertCircuit(allCircuits)
        persistence.overviewDao().insertAll(allOverview)
        persistence.scheduleDao().replaceAllForSeason(season, scheduled)

        return@attempt true
    }

    fun getOverview(): Flow<List<OverviewRace>> {
        return persistence.overviewDao().getOverview()
            .map { list -> list.map { overviewMapper.mapOverview(it) } }
    }

    fun getOverview(season: Int): Flow<Overview> {
        return persistence.overviewDao().getOverview(season)
            .map { overview ->
                Overview(
                    season = season,
                    overviewRaces = overview
                        .filterNotNull()
                        .map { overviewMapper.mapOverview(it) }
                )
            }
    }
}