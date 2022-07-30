package tmg.flashback.configuration.usecases

import android.content.Context
import android.util.Log
import androidx.work.*
import org.threeten.bp.LocalTime
import tmg.flashback.configuration.workmanager.ConfigSyncJob
import tmg.utilities.extensions.secondsToHHmm
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ConfigSyncUseCase @Inject constructor(
    private val applicationContext: Context
) {

    fun schedule() {
        val delaySeconds = delayUntilEndOfDay()

        val (hours, mins) = delaySeconds.toInt().secondsToHHmm
        Log.i("WorkerProvider", "contentSync() with initialDelay of ($delaySeconds) $hours hrs $mins mins")

        val request = PeriodicWorkRequestBuilder<ConfigSyncJob>(1, TimeUnit.DAYS, 6, TimeUnit.HOURS)
            .setConstraints(
                Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
            .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork("CONTENT_SYNC", ExistingPeriodicWorkPolicy.REPLACE, request)
    }

    private fun delayUntilEndOfDay(): Long {
        val delay = LocalTime.now()
        var delaySeconds = 24L * 60L * 60L
        delaySeconds -= (delay.hour * 60 * 60)
        delaySeconds -= (delay.minute * 60)
        delaySeconds -= (delay.second)
        return delaySeconds
    }
}