package tmg.flashback.statistics.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import tmg.flashback.configuration.usecases.FetchConfigUseCase
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.repo.OverviewRepository
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase

@Suppress("EXPERIMENTAL_API_USAGE")
class ContentSyncWorker(
    private val fetchConfigUseCase: FetchConfigUseCase,
    private val defaultSeasonUseCase: DefaultSeasonUseCase,
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
        fetchConfigUseCase.fetchAndApply()

        // Get latest season info
        overviewRepository.fetchOverview(defaultSeasonUseCase.defaultSeason)

        // Schedule notification updating
        workerProvider.schedule()
        
        return Result.success()
    }
}
