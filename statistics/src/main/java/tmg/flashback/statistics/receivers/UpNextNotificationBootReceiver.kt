package tmg.flashback.statistics.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tmg.flashback.statistics.controllers.ScheduleController

@KoinApiExtension
class UpNextNotificationBootReceiver: BroadcastReceiver(), KoinComponent {

    private val scheduleController: ScheduleController by inject()

    override fun onReceive(context: Context?, intent: Intent?) {

        // Reschedule notifications
        Log.i("Flashback", "Rescheduling notifications for upcoming events")
        GlobalScope.launch { scheduleController.scheduleNotifications(true) }
    }
}