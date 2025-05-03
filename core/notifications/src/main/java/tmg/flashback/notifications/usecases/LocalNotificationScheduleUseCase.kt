package tmg.flashback.notifications.usecases

import android.util.Log
import java.time.LocalDateTime
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.repository.NotificationIdsRepository
import tmg.utilities.extensions.format
import javax.inject.Inject

class LocalNotificationScheduleUseCase @Inject constructor(
    private val notificationIdsRepository: NotificationIdsRepository,
    private val alarmManager: SystemAlarmManager
) {
    fun schedule(
        requestCode: Int,
        channelId: String,
        title: String,
        text: String,
        timestamp: LocalDateTime,
        exact: Boolean = true
    ) {
        alarmManager.schedule(requestCode, channelId, title, text, timestamp, exact)
        notificationIdsRepository.notificationIds = notificationIdsRepository.notificationIds
            .toMutableSet()
            .apply { add(requestCode) }

        Log.d("Notification", "Scheduled notification at ${timestamp.format("dd MMM yyyy HH:mm")} $title / $text - $requestCode")
    }
}