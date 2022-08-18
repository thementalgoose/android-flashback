package tmg.flashback.stats.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.stats.usecases.ScheduleNotificationsUseCase
import javax.inject.Inject

@AndroidEntryPoint
class UpNextNotificationBootReceiver: BroadcastReceiver() {

    @Inject
    protected lateinit var upNextNotificationBootReceiver: UpNextNotification

    override fun onReceive(context: Context?, intent: Intent?) {

        // Reschedule notifications
        Log.i("Notifications", "Scheduling upcoming notifications")
        if (context != null && intent != null) {
            upNextNotificationBootReceiver.onReceive(context, intent)
        }
    }
}


class UpNextNotification @Inject constructor(
    private val scheduleNotificationsUseCase: ScheduleNotificationsUseCase
) {
    fun onReceive(context: Context, intent: Intent) {

        // Reschedule notifications
        Log.i("Notifications", "Scheduling upcoming notifications")
        scheduleNotificationsUseCase.schedule()
    }
}