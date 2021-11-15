package tmg.flashback.statistics.repo

import org.threeten.bp.LocalDate
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.formula1.model.OverviewRace
import tmg.flashback.statistics.repo.base.BaseRepository
import tmg.flashback.statistics.repo.mappers.app.OverviewMapper
import tmg.flashback.statistics.room.FlashbackDatabase

class ScheduleRepository(
    private val persistence: FlashbackDatabase,
    crashController: CrashController,
    private val overviewRepository: OverviewRepository,
    private val overviewMapper: OverviewMapper
): BaseRepository(crashController) {

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