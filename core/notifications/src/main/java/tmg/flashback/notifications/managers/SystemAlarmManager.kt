package tmg.flashback.notifications.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.AlarmManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import tmg.flashback.crashlytics.manager.CrashlyticsManager
import tmg.flashback.device.repository.PermissionRepository
import tmg.flashback.notifications.receiver.LocalNotificationBroadcastReceiver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemAlarmManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val crashController: CrashlyticsManager
) {
    private val alarmManager: AlarmManager? by lazy {
        applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
    }

    /**
     * Schedule a pending intent with a title and description
     */
    fun schedule(
        requestCode: Int,
        channelId: String,
        requestText: String,
        requestDescription: String,
        requestTimestamp: LocalDateTime,
        exact: Boolean
    ) {
        val alarmManager: AlarmManager = alarmManager ?: run {
            crashController.logException(NullPointerException("Alarm Manager null when scheduling alarm"), "Alarm Manager null when scheduling alarm")
            return
        }
        val pendingIntent = pendingIntent(applicationContext, channelId, requestCode, requestText, requestDescription)

        val instant = requestTimestamp.toInstant(ZoneOffset.UTC)
        Log.d("Notification", "Scheduling (exact=$exact) alarm wakeup for ${instant.toEpochMilli()} (current system is ${System.currentTimeMillis()}, with millis diff being ${(instant.toEpochMilli() - System.currentTimeMillis()) / 1000} seconds)")
        when (exact) {
            true -> AlarmManagerCompat.setExactAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                instant.toEpochMilli(),
                pendingIntent
            )
            false -> AlarmManagerCompat.setAndAllowWhileIdle(
                alarmManager,
                AlarmManager.RTC_WAKEUP,
                instant.toEpochMilli(),
                pendingIntent
            )
        }
    }

    /**
     * Cancel a pending intent via. the request code
     */
    fun cancel(requestCode: Int) {
        val alarmManager: AlarmManager = alarmManager ?: run {
            crashController.logException(NullPointerException("Alarm Manager null when cancelling alarm"), "Alarm Manager null when cancelling alarm")
            return
        }

        val pendingIntent = pendingIntent(applicationContext, "", requestCode, "", "")
        alarmManager.cancel(pendingIntent)
    }

    private fun pendingIntent(context: Context, channelId: String, requestCode: Int, title: String, description: String): PendingIntent {
        val localNotificationReceiverIntent = LocalNotificationBroadcastReceiver.intent(context,
            channelId = channelId,
            title = title,
            description = description
        )
        return PendingIntent.getBroadcast(applicationContext, requestCode, localNotificationReceiverIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }
}