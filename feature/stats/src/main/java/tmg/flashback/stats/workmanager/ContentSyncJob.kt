package tmg.flashback.stats.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.stats.usecases.DefaultSeasonUseCase
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase

@HiltWorker
class ContentSyncJob @AssistedInject constructor(
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
    private val overviewRepository: OverviewRepository,
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase,
    @Assisted context: Context,
    @Assisted params: WorkerParameters
): CoroutineWorker(
    appContext = context,
    params = params
) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        if (isStopped) {
            return@withContext Result.success()
        }

        // Get latest season info
        overviewRepository.fetchOverview(defaultSeasonUseCase.defaultSeason)

        // Schedule notifications
        scheduleNotificationsUseCase.schedule()

        return@withContext Result.success()
    }
}