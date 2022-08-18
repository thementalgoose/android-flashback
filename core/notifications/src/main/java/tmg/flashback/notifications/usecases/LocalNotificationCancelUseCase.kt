package tmg.flashback.notifications.usecases

import android.util.Log
import tmg.flashback.notifications.BuildConfig
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.repository.NotificationRepository
import javax.inject.Inject

class LocalNotificationCancelUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val alarmManager: SystemAlarmManager
) {
    fun cancel(requestCode: Int) {
        alarmManager.cancel(requestCode)
        notificationRepository.notificationIds = notificationRepository.notificationIds
            .toMutableSet()
            .apply {
                remove(requestCode)
            }

        if (BuildConfig.DEBUG) {
            Log.d("Notification", "Cancelled $requestCode, leaving ${notificationRepository.notificationIds}")
        }
    }

    fun cancelAll() {
        if (BuildConfig.DEBUG) {
            Log.d("Notifications", "Cancelling all (all = ${notificationRepository.notificationIds})")
        }
        notificationRepository.notificationIds
            .forEach { requestCode ->
                alarmManager.cancel(requestCode)
            }
        notificationRepository.notificationIds = emptySet()
    }
}