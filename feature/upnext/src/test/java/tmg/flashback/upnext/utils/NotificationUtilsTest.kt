package tmg.flashback.upnext.utils

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import tmg.flashback.formula1.model.Timestamp
import tmg.flashback.upnext.R
import tmg.flashback.upnext.model.NotificationChannel
import tmg.flashback.upnext.model.NotificationReminder
import tmg.flashback.upnext.utils.NotificationUtils.getCategoryBasedOnLabel
import tmg.flashback.upnext.utils.NotificationUtils.getNotificationTitleText
import tmg.flashback.upnext.utils.NotificationUtils.getRequestCode

internal class NotificationUtilsTest {

    @ParameterizedTest(name = "get request code for season {0} round {1} value {2} returns {3}")
    @CsvSource(
        "01-01-2021-12:00,1609502400",
        "30-12-2025-13:39,1767101940"
    )
    fun `get request code for season round and value returns unique request code`(localDateTimeString: String, expected: Int) {
        val localDateTime = LocalDateTime.parse(localDateTimeString, DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm"))
        assertEquals(expected, getRequestCode(localDateTime))
    }

    @ParameterizedTest(name = "label of {0} returns channel {1}")
    @CsvSource(
        "Grand Prix,RACE",
        "raCe,RACE",
        "race,RACE",

        "quali,QUALIFYING",
        "sprint quali,QUALIFYING",
        "sprinting,QUALIFYING",
        "sprint qualifying,QUALIFYING",
        "quali,QUALIFYING",
        "qualifying,QUALIFYING",
        "sprinter,QUALIFYING",

        "fp,FREE_PRACTICE",
        "FP2,FREE_PRACTICE",
        "FP4,FREE_PRACTICE",
        "Fp1,FREE_PRACTICE",
        "free practice,FREE_PRACTICE",
        "practice,FREE_PRACTICE",

        "free,SEASON_INFO",
        "day,SEASON_INFO",
        "winter test day 3,SEASON_INFO",
        "unveil 3,SEASON_INFO",
    )
    fun `get category based on label returns expected`(label: String, expectedNotificationChannel: NotificationChannel) {
        assertEquals(expectedNotificationChannel, getCategoryBasedOnLabel(label))
    }

    @Test
    fun `getting notification title text returns expected result`() {

        val mockContext: Context = mockk(relaxed = true)

        val inputTitle: String = "Jordan Grand Prix"
        val inputLabel: String = "Qualifying"
        val inputTimestamp: Timestamp = Timestamp(LocalDate.now(), LocalTime.of(12, 0))
        val inputNotificationReminder: NotificationReminder = NotificationReminder.MINUTES_60

        val expectedOutputTitle: String = "Qualifying starts in 1 hour"
        val expectedOutputText: String = "Jordan Grand Prix Qualifying starts in 1 hour at 12:00 Europe/London (device time)"

        mockkStatic(ZoneId::class)
        every { ZoneId.systemDefault() } returns ZoneId.of("Europe/London")
        every { mockContext.getString(R.string.notification_content_title, inputLabel, "1 hour") } returns "$inputLabel starts in 1 hour"
        every { mockContext.getString(R.string.notification_content_text_device_time, any(), any()) } returns "12:00 Europe/London (device time)"
        every { mockContext.getString(R.string.notification_content_text, inputTitle, inputLabel, "1 hour", "12:00 Europe/London (device time)") } returns "$inputTitle $inputLabel starts in 1 hour at 12:00 Europe/London (device time)"
        every { mockContext.getString(any()) } returns "1 hour"

        val (title, text) = getNotificationTitleText(mockContext, inputTitle, inputLabel, inputTimestamp, inputNotificationReminder)

        assertEquals(expectedOutputTitle, title)
        assertEquals(expectedOutputText, text)

        verify {
            mockContext.getString(inputNotificationReminder.label)
            mockContext.getString(R.string.notification_content_title, inputLabel, "1 hour")
            mockContext.getString(R.string.notification_content_text_device_time, any(), any())
            mockContext.getString(R.string.notification_content_text, inputTitle, inputLabel, "1 hour", "12:00 Europe/London (device time)")
        }
    }
}