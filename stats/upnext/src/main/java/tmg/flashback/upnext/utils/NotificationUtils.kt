package tmg.flashback.upnext.utils

import android.content.Context
import java.util.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.TemporalField
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.R
import tmg.flashback.upnext.model.NotificationChannel
import tmg.flashback.upnext.model.NotificationReminder

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

        var deviceDateTime: LocalDateTime? = null
        timestamp.on(
            dateAndTime = { utc, local ->
                deviceDateTime = local
            }
        )

        val deviceTimeString = if (deviceDateTime != null) {
            val timeString = deviceDateTime!!.format(DateTimeFormatter.ofPattern("HH:mm"))
            context.getString(R.string.notification_content_text_device_time, timeString, ZoneId.systemDefault().id)
        } else {
            ""
        }
        val notificationText = context.getString(R.string.notification_content_text, title, label, reminderString, deviceTimeString)

        return Pair(notificationTitle, notificationText)
    }
}