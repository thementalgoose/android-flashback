package tmg.flashback.notifications.managers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import tmg.flashback.crash_reporting.controllers.CrashController
import tmg.flashback.notifications.R
import tmg.flashback.notifications.model.NotificationPriority
import tmg.flashback.notifications.navigation.NotificationNavigationProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemNotificationManager @Inject constructor(
    @ApplicationContext
    private val applicationContext: Context,
    private val crashController: CrashController,
    private val navigationProvider: NotificationNavigationProvider
) {

    private val notificationManager: NotificationManager? by lazy {
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    /**
     * Build a notification object to be displayed to the user
     *
     * @param context Defaults to application context but should be supplied from the broadcast receiver
     */
    fun buildNotification(
        context: Context?,
        channelId: String,
        title: String,
        text: String,
        priority: NotificationPriority = NotificationPriority.DEFAULT,
        @DrawableRes icon: Int = R.drawable.ic_notification
    ): Notification {
        val contextToUse = context ?: applicationContext
        val intent = navigationProvider.relaunchAppIntent(contextToUse)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0 /* Request code */,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultAlarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(icon)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentTitle(title)
            .setContentText(text)
            .setSound(defaultAlarmSound)
            .setPriority(when (priority) {
                NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
                NotificationPriority.DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
            })
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        return notificationBuilder.build()
    }

    /**
     * Notify a notification to the system
     */
    fun notify(tag: String, id: Int, notification: Notification) {
        notificationManager?.notify(tag, id, notification) ?: crashController.logException(NullPointerException("Notification Manager null when notifying ($tag,$id)"), "Notification Manager null when notifying ($tag,$id)")
    }

    /**
     * Cancel a specific notification
     */
    fun cancel(tag: String, id: Int) {
        notificationManager?.cancel(tag, id) ?: crashController.logException(NullPointerException("Notification Manager null when cancelling ($tag,$id)"), "Notification Manager null when cancelling ($tag,$id)")
    }

    /**
     * Cancel all active notifications
     */
    fun cancelAll() {
        notificationManager?.cancelAll() ?: crashController.logException(NullPointerException("Notification Manager null when cancelling all"), "Notification Manager null when cancelling all")
    }

    /**
     * Creates a notification channel
     */
    fun createChannel(channelId: String, @StringRes title: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                applicationContext.getString(title),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * Cancels a notification channel
     */
    fun cancelChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager?.deleteNotificationChannel(channelId)
        }
    }
}