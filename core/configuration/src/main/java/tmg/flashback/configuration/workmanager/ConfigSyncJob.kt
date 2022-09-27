package tmg.flashback.configuration.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tmg.flashback.configuration.usecases.FetchConfigUseCase

@HiltWorker
class ConfigSyncJob @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val fetchConfigUseCase: FetchConfigUseCase
): CoroutineWorker(
    appContext = context,
    params = params
) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        if (isStopped) {
            return@withContext Result.success()
        }

        // Remote config sync
        fetchConfigUseCase.fetchAndApply()

        return@withContext Result.success()
    }
}