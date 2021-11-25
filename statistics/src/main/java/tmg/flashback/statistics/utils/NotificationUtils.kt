package tmg.flashback.statistics.utils

import android.content.Context
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.statistics.R
import tmg.flashback.statistics.repository.models.NotificationReminder

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
        val notificationTitle = context.getString(R.string.notification_content_title, label, reminderString)

        val deviceDateTime: LocalDateTime = timestamp.deviceLocalDateTime

        val timeString = deviceDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        val deviceTimeString = context.getString(R.string.notification_content_text_device_time, timeString, ZoneId.systemDefault().id)
        val notificationText = context.getString(R.string.notification_content_text, title, label, reminderString, deviceTimeString)

        return Pair(notificationTitle, notificationText)
    }
}