package tmg.flashback.domain.repo

import org.threeten.bp.LocalDate
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.managers.NetworkConnectivityManager
import tmg.flashback.domain.persistence.FlashbackDatabase
import tmg.flashback.domain.repo.base.BaseRepository
import tmg.flashback.domain.repo.mappers.app.OverviewMapper
import tmg.flashback.formula1.model.OverviewRace
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val persistence: FlashbackDatabase,
    crashController: CrashlyticsManager,
    networkConnectivityManager: NetworkConnectivityManager,
    private val overviewRepository: OverviewRepository,
    private val overviewMapper: OverviewMapper
): BaseRepository(crashController, networkConnectivityManager) {

    /**
     * overview.json
     * Fetch overview data for all seasons currently available
     */
    suspend fun fetchOverview(): Boolean {
        return overviewRepository.fetchOverview()
    }

    suspend fun getUpcomingEvents(fromDate: LocalDate = LocalDate.now()): List<OverviewRace> {
        return persistence.scheduleDao().getUpcomingEvents(fromDate)
            .map { overview -> overviewMapper.mapOverview(overview) }
    }
}