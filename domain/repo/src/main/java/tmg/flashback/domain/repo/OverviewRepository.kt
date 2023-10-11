package tmg.flashback.domain.repo

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.persistence.FlashbackDatabase
import tmg.flashback.domain.repo.base.BaseRepository
import tmg.flashback.domain.repo.mappers.app.OverviewMapper
import tmg.flashback.domain.repo.mappers.network.NetworkCircuitDataMapper
import tmg.flashback.domain.repo.mappers.network.NetworkOverviewMapper
import tmg.flashback.domain.repo.mappers.network.NetworkScheduleMapper
import tmg.flashback.flashbackapi.api.api.FlashbackApi
import tmg.flashback.formula1.model.Overview
import tmg.flashback.formula1.model.OverviewRace
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OverviewRepository @Inject constructor(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashlyticsManager,
    networkConnectivityManager: NetworkConnectivityManager,
    private val overviewMapper: OverviewMapper,
    private val networkOverviewMapper: NetworkOverviewMapper,
    private val networkCircuitDataMapper: NetworkCircuitDataMapper,
    private val networkScheduleMapper: NetworkScheduleMapper
): BaseRepository(crashController, networkConnectivityManager) {

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

        val maxRoundRemote = allOverview.maxOfOrNull { it.round } ?: 0
        val maxRoundLocal = persistence.overviewDao().getLastRound(season)?.overview?.round ?: 0
        if (maxRoundLocal > maxRoundRemote) {
            // We need to remove old rounds!
            for (round in (maxRoundRemote + 1) until maxRoundLocal + 1) {
                Log.i("Overview", "Deleting season $season round $round overview")
                persistence.overviewDao().deleteOverview(season, round)
            }
        }

        return@attempt true
    }

    fun getOverview(): Flow<List<OverviewRace>> {
        return persistence.overviewDao().getOverview()
            .map { list -> list.map { overviewMapper.mapOverview(it) } }
    }

    fun getOverview(season: Int, round: Int): Flow<OverviewRace?> {
        return persistence.overviewDao().getOverview(season, round)
            .map {
                if (it == null) return@map null
                try {
                    overviewMapper.mapOverview(it)
                } catch (e: RuntimeException) {
                    null
                }
            }
    }

    fun getOverview(season: Int): Flow<Overview> {
        return persistence.overviewDao().getOverview(season)
            .map { overview ->
                Overview(
                    season = season,
                    overviewRaces = overview
                        .map { overviewMapper.mapOverview(it) }
                        .sortedBy { it.round }
                )
            }
    }
}