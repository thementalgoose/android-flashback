package tmg.flashback.notifications.usecases

import android.util.Log
import tmg.flashback.notifications.BuildConfig
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.repository.NotificationIdsRepository
import javax.inject.Inject

class LocalNotificationCancelUseCase @Inject constructor(
    private val notificationIdsRepository: NotificationIdsRepository,
    private val alarmManager: SystemAlarmManager
) {
    fun cancel(requestCode: Int) {
        alarmManager.cancel(requestCode)
        notificationIdsRepository.notificationIds = notificationIdsRepository.notificationIds
            .toMutableSet()
            .apply {
                remove(requestCode)
            }

        if (BuildConfig.DEBUG) {
            Log.d("Notification", "Cancelled $requestCode, leaving ${notificationIdsRepository.notificationIds}")
        }
    }

    fun cancelAll() {
        if (BuildConfig.DEBUG) {
            Log.d("Notifications", "Cancelling all (all = ${notificationIdsRepository.notificationIds})")
        }
        notificationIdsRepository.notificationIds
            .forEach { requestCode ->
                alarmManager.cancel(requestCode)
            }
        notificationIdsRepository.notificationIds = emptySet()
    }
}