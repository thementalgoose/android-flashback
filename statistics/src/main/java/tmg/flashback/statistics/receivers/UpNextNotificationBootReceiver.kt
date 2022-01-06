package tmg.flashback.statistics.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.statistics.workmanager.WorkerProvider

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
    private val workerProvider: WorkerProvider by inject()

    fun onReceive(context: Context, intent: Intent) {

        // Reschedule notifications
        Log.i("Notifications", "Scheduling upcoming notifications")
        workerProvider.schedule()
    }
}