package tmg.flashback.statistics.workmanager

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class NotificationSchedulerProvider(
    private val applicationContext: Context
) {
    fun schedule() {
        val request = OneTimeWorkRequestBuilder<NotificationScheduler>()
            .apply {
                addTag("NOTIFICATIONS")
                setInputData(Data.Builder().putBoolean("force", true).build())
            }
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork("NOTIFICATIONS", ExistingWorkPolicy.REPLACE, request)
    }
}