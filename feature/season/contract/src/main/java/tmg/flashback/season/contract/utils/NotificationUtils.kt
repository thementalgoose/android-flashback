package tmg.flashback.season.contract.utils

import android.content.Context
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.season.contract.R
import tmg.flashback.strings.R.string
import tmg.flashback.season.contract.repository.models.NotificationReminder
import java.util.Locale

object NotificationUtils {

    /**
     * Generate a unique request code per timestamp
     */
    fun getRequestCode(date: LocalDateTime): Int {
        return date
            .toEpochSecond(ZoneOffset.UTC)
            .coerceIn(0, Int.MAX_VALUE.toLong())
            .toInt()
    }

    /**
     * Get the content of the notification
     * - Race starts in 30 minutes
     * - Russian Grand Prix Race starts in 30 minutes at 12:30 Europe/London (device time)
     */
    fun getNotificationTitleText(context: Context, title: String, label: String, timestamp: Timestamp, notificationReminder: NotificationReminder): Pair<String, String> {
        val reminderString = context.getString(notificationReminder.label)
        val notificationTitle = context.getString(string.notification_content_title, label, reminderString)

        val deviceDateTime: LocalDateTime = timestamp.deviceLocalDateTime

        val timeString = deviceDateTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale.UK))
        val deviceTimeString = context.getString(string.notification_content_text_device_time, timeString, ZoneId.systemDefault().id)
        val notificationText = context.getString(string.notification_content_text, title, label, reminderString, deviceTimeString)

        return Pair(notificationTitle, notificationText)
    }

    /**
     * Get the content of the notification
     * - Race starts in 30 minutes
     * - Russian Grand Prix Race starts at 12:30 Europe/London (device time)
     */
    fun getInexactNotificationTitleText(context: Context, title: String, label: String, timestamp: Timestamp): Pair<String, String> {
        val deviceDateTime: LocalDateTime = timestamp.deviceLocalDateTime
        val timeString = deviceDateTime.format(DateTimeFormatter.ofPattern("HH:mm", Locale.UK))

        val notificationTitle = context.getString(string.notification_content_title_inexact, label, timeString)
        val deviceTimeString = context.getString(string.notification_content_text_device_time, timeString, ZoneId.systemDefault().id)
        val notificationText = context.getString(string.notification_content_text_inexact, title, label, deviceTimeString)

        return Pair(notificationTitle, notificationText)
    }
}