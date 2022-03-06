package tmg.flashback.notifications.usecases

import android.util.Log
import org.threeten.bp.LocalDateTime
import tmg.flashback.notifications.BuildConfig
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.repository.NotificationRepository
import tmg.utilities.extensions.format

class LocalNotificationScheduleUseCase(
    private val notificationRepository: NotificationRepository,
    private val alarmManager: SystemAlarmManager
) {
    fun schedule(
        requestCode: Int,
        channelId: String,
        title: String,
        text: String,
        timestamp: LocalDateTime
    ) {
        alarmManager.schedule(requestCode, channelId, title, text, timestamp)
        notificationRepository.notificationIds = notificationRepository.notificationIds
            .toMutableSet()
            .apply { add(requestCode) }

        if (BuildConfig.DEBUG) {
            Log.d("Notification", "Scheduled notification at ${timestamp.format("dd MMM yyyy HH:mm")} $title / $text - $requestCode")
        }
    }
}