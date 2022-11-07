package tmg.flashback.usecases

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import tmg.flashback.BuildConfig
import tmg.flashback.configuration.usecases.ApplyConfigUseCase
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.repo.RaceRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCase
import javax.inject.Inject

class DashboardSyncUseCase @Inject constructor(
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val fetchConfigUseCase: FetchConfigUseCase,
    private val applyConfigUseCase: ApplyConfigUseCase,
    private val raceRepository: RaceRepository,
    private val overviewRepository: OverviewRepository,
    private val updateWidgetsUseCase: UpdateWidgetsUseCase
) {
    suspend fun sync(): Boolean = coroutineScope {
        val all = awaitAll(
            async { syncConfig() },
            async { syncRaces() },
            async { syncOverview() }
        )
        return@coroutineScope all.reduce { a, b -> a && b }
    }

    private suspend fun syncConfig(): Boolean {
        fetchConfigUseCase.fetch()
        val activate = applyConfigUseCase.apply()
        if (BuildConfig.DEBUG) {
            Log.i("Dashboard", "Remote config change detected $activate")
        }
        if (activate) {
            scheduleNotificationsUseCase.schedule()
            updateWidgetsUseCase.update()
            return true
        }
        return false
    }

    private suspend fun syncRaces(): Boolean {
        return raceRepository.fetchRaces(defaultSeasonUseCase.defaultSeason)
    }

    private suspend fun syncOverview(): Boolean {
        return overviewRepository.fetchOverview(defaultSeasonUseCase.defaultSeason)
    }
}