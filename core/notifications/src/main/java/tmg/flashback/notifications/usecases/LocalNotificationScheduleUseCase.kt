package tmg.flashback.notifications.usecases

import android.util.Log
import java.time.LocalDateTime
import tmg.flashback.notifications.BuildConfig
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.utilities.extensions.format
import javax.inject.Inject

class LocalNotificationScheduleUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
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
        notificationRepository.notificationIds = notificationRepository.notificationIds
            .toMutableSet()
            .apply { add(requestCode) }

        Log.d("Notification", "Scheduled notification at ${timestamp.format("dd MMM yyyy HH:mm")} $title / $text - $requestCode")
    }
}