package tmg.flashback.statistics.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*
import androidx.work.OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.statistics.BuildConfig
import tmg.flashback.statistics.extensions.secondsToHHmm
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.DAYS

class WorkerProvider(
    private val applicationContext: Context
) {
    fun schedule() {
        Log.i("WorkerProvider", "schedule()")
        val request = OneTimeWorkRequestBuilder<NotificationScheduleWorker>()
            .apply {
                addTag("NOTIFICATIONS")
                setInputData(Data.Builder().putBoolean("force", true).build())
                setExpedited(RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            }
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniqueWork("NOTIFICATIONS", ExistingWorkPolicy.REPLACE, request)
    }

    fun contentSync() {

        val delay = LocalTime.now()

        var delaySeconds = 24L * 60L * 60L
        delaySeconds -= (delay.hour * 60 * 60)
        delaySeconds -= (delay.minute * 60)
        delaySeconds -= (delay.second)

        if (BuildConfig.DEBUG) {
            val (hours, mins) = delaySeconds.toInt().secondsToHHmm
            Log.i("WorkerProvider", "contentSync() with initialDelay of ($delaySeconds) $hours hrs $mins mins ${delay.format(DateTimeFormatter.ofPattern("HH:mm:ss"))}")
        }

        val request = PeriodicWorkRequestBuilder<ContentSyncWorker>(1, DAYS, 6, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build())
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork("CONTENT_SYNC", ExistingPeriodicWorkPolicy.REPLACE, request)
    }
}