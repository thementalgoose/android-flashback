package tmg.flashback.configuration.workmanager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.core.component.KoinComponent
import tmg.flashback.configuration.usecases.FetchConfigUseCase

class ConfigSyncJob(
    private val fetchConfigUseCase: FetchConfigUseCase,
    context: Context,
    params: WorkerParameters
): CoroutineWorker(
    appContext = context,
    params = params
), KoinComponent {

    override suspend fun doWork(): Result {
        // Remote config sync
        fetchConfigUseCase.fetchAndApply()

        return Result.success()
    }
}