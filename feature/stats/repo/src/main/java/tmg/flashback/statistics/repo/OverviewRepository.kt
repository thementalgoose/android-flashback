package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.SeasonOverview
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.models.overview.Overview
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.OverviewMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkOverviewMapper
import tmg.flashback.statistics.room.FlashbackDatabase
import java.lang.RuntimeException

class OverviewRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val overviewMapper: OverviewMapper,
    private val networkOverviewMapper: NetworkOverviewMapper
): BaseRepository(crashController) {

    /**
     * overview.json
     * Fetch overview data for all seasons currently available
     */
    suspend fun fetchOverview(): Boolean = attempt(
        apiCall = suspend { api.getOverview() },
        msgIfFailed = "overview.json"
    ) { data ->
        val allOverview = data.values
            .mapNotNull { networkOverviewMapper.mapOverview(it) }
        persistence.overviewDao().insertAll(allOverview)
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
        val allOverview = data.values
            .mapNotNull { networkOverviewMapper.mapOverview(it) }
        persistence.overviewDao().insertAll(allOverview)
        return@attempt true
    }

    fun getOverview(season: Int): Flow<SeasonOverview> {
        return persistence.overviewDao().getOverview(season)
            .map { overview ->
                SeasonOverview(
                    season = season,
                    roundOverviews = overview
                        .filterNotNull()
                        .map { overviewMapper.mapOverview(it) }
                )
            }
    }
}