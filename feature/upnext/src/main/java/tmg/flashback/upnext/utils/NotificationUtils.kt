package tmg.flashback.upnext.utils

import java.util.*
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.temporal.TemporalField
import tmg.flashback.upnext.model.NotificationChannel

object NotificationUtils {
    fun getRequestCode(date: LocalDateTime): Int {
        return date
            .toEpochSecond(ZoneOffset.UTC)
            .coerceIn(0, Int.MAX_VALUE.toLong())
            .toInt()
    }

    fun getCategoryBasedOnLabel(label: String): NotificationChannel {
        return when {
            label.includes(
                "race",
                "grand prix"
            ) -> NotificationChannel.RACE
            label.includes(
                "qualifying",
                "sprint qualifying",
                "sprint quali",
                "sprint",
                "quali"
            ) -> NotificationChannel.QUALIFYING
            label.includes(
                "fp",
                "free practice",
                "practice"
            ) -> NotificationChannel.FREE_PRACTICE
            else -> NotificationChannel.SEASON_INFO
        }
    }

    private fun String.includes(vararg partials: String): Boolean {
        return partials.any { label ->
            this.lowercase().contains(label.lowercase())
        }
    }
}