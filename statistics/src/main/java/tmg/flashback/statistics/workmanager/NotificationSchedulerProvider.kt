package tmg.flashback.statistics.workmanager

import android.content.Context
import androidx.work.*
import androidx.work.OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST

class NotificationSchedulerProvider(
    private val applicationContext: Context
) {
    fun schedule() {
        val request = OneTimeWorkRequestBuilder<NotificationScheduler>()
            .apply {
                addTag("NOTIFICATIONS")
                setInputData(Data.Builder().putBoolean("force", true).build())
                setExpedited(RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            }
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniqueWork("NOTIFICATIONS", ExistingWorkPolicy.REPLACE, request)
    }
}