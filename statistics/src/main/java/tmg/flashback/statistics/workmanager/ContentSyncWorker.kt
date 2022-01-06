package tmg.flashback.statistics.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import tmg.flashback.configuration.controllers.ConfigController
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.repo.OverviewRepository

@Suppress("EXPERIMENTAL_API_USAGE")
class ContentSyncWorker(
    private val configController: ConfigController,
    private val homeController: HomeController,
    private val overviewRepository: OverviewRepository,
    private val workerProvider: WorkerProvider,
    context: Context,
    parameters: WorkerParameters
): CoroutineWorker(
    context,
    parameters
), KoinComponent {

    override suspend fun doWork(): Result {

        // Remote config sync
        val remoteConfigResult = configController.fetchAndApply()

        // Get latest season info
        val currentOverviewResult = overviewRepository.fetchOverview(homeController.defaultSeason)

        // Schedule notification updating
        workerProvider.schedule()

        return if (remoteConfigResult && currentOverviewResult) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}