package tmg.flashback.stats.usecases

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.stats.workmanager.ScheduleNotificationsJob
import javax.inject.Inject

class ScheduleNotificationsUseCase @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context
) {
    fun schedule() {

        val request = OneTimeWorkRequestBuilder<ScheduleNotificationsJob>()
            .apply {
                addTag("NOTIFICATIONS")
                setInputData(Data.Builder().putBoolean("force", true).build())
            }
            .build()

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniqueWork("NOTIFICATIONS", ExistingWorkPolicy.REPLACE, request)
    }
}