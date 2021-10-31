package tmg.flashback.statistics.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tmg.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.History
import tmg.flashback.formula1.model.HistoryRound
import tmg.flashback.statistics.network.api.FlashbackApi
import tmg.flashback.statistics.network.models.overview.Overview
import tmg.flashback.statistics.network.utils.data
import tmg.flashback.statistics.network.utils.hasData
import tmg.flashback.statistics.repo.mappers.app.OverviewMapper
import tmg.flashback.statistics.repo.mappers.network.NetworkOverviewMapper
import tmg.flashback.statistics.room.FlashbackDatabase
import java.lang.RuntimeException

class OverviewRepository(
    private val api: FlashbackApi,
    private val persistence: FlashbackDatabase,
    private val crashController: CrashController,
    private val overviewMapper: OverviewMapper,
    private val networkOverviewMapper: NetworkOverviewMapper
) {
    suspend fun fetchOverview(): Boolean {
        val result = api.getOverview()
        if (!result.hasData) {
            return false
        }
        return fetchOverviews(result.data())
    }

    suspend fun fetchOverview(season: Int): Boolean {
        val result = api.getOverview(season)
        if (!result.hasData) {
            return false
        }
        return fetchOverviews(result.data())
    }

    private suspend fun fetchOverviews(data: Overview?): Boolean {

        val allOverviews = (data?.values ?: emptyList())
            .mapNotNull {
                try {
                    networkOverviewMapper.mapOverview(it)
                } catch (e: RuntimeException) {
                    crashController.logException(e, "fetchOverview failed to parse ${it.season} / ${it.round}")
                    null
                }
            }
        persistence.overviewDao().insertAll(allOverviews)
        return allOverviews.isNotEmpty()
    }

    fun getOverview(season: Int): Flow<History> {
        return persistence.overviewDao().getOverview(season)
            .map { overview ->
                History(
                    season = season,
                    rounds = overview
                        .filterNotNull()
                        .map { overviewMapper.mapOverview(it) }
                )
            }
    }
}