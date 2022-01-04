package tmg.flashback.statistics.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.crash_reporting.repository.CrashRepository
import tmg.flashback.prefs.manager.PreferenceManager
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.workmanager.NotificationScheduler
import tmg.flashback.statistics.workmanager.NotificationSchedulerProvider

class UpNextNotificationBootReceiver: BroadcastReceiver() {

    private val upNextNotificationBootReceiver: UpNextNotification by lazy { UpNextNotification() }

    override fun onReceive(context: Context?, intent: Intent?) {

        // Reschedule notifications
        Log.i("Notifications", "Scheduling upcoming notifications")
        if (context != null && intent != null) {
            upNextNotificationBootReceiver.onReceive(context, intent)
        }
    }
}

@KoinApiExtension
class UpNextNotification: KoinComponent {
    private val notificationSchedulerProvider: NotificationSchedulerProvider by inject()

    fun onReceive(context: Context, intent: Intent) {

        // Reschedule notifications
        Log.i("Notifications", "Scheduling upcoming notifications")
        notificationSchedulerProvider.schedule()
    }
}