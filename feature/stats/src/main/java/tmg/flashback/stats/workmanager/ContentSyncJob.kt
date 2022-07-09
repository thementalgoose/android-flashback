package tmg.flashback.stats.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase

class ContentSyncJob(
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    context: Context,
    params: WorkerParameters
): CoroutineWorker(
    appContext = context,
    params = params
), KoinComponent {
    override suspend fun doWork(): Result {

        // Get latest season info
        overviewRepository.fetchOverview(defaultSeasonUseCase.defaultSeason)

        // Schedule notifications
        scheduleNotificationsUseCase.schedule()

        return Result.success()
    }
}