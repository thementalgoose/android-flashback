package tmg.flashback.notifications.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.threeten.bp.LocalDateTime
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.repository.NotificationRepository

internal class LocalNotificationScheduleUseCaseTest {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)
    private val mockAlarmManager: SystemAlarmManager = mockk(relaxed = true)

    private lateinit var underTest: LocalNotificationScheduleUseCase

    private fun initUnderTest() {
        underTest = LocalNotificationScheduleUseCase(
            mockNotificationRepository,
            mockAlarmManager
        )
    }

    @Test
    fun `scheduling notification sends inputs to alarm manager`() {
        val timestamp = LocalDateTime.of(2020, 1, 1, 1, 1)

        initUnderTest()
        underTest.schedule(
            requestCode = 1,
            channelId = "2",
            title = "3",
            text = "4",
            timestamp = timestamp
        )

        verify {
            mockAlarmManager.schedule(
                requestCode = 1,
                channelId = "2",
                requestText = "3",
                requestDescription = "4",
                requestTimestamp = timestamp
            )
        }
    }

    @Test
    fun `scheduling notification updates request code notification ids`() {
        every { mockNotificationRepository.notificationIds } returns setOf(102)
        val timestamp = LocalDateTime.of(2020, 1, 1, 1, 1)

        initUnderTest()
        underTest.schedule(
            requestCode = 1,
            channelId = "2",
            title = "3",
            text = "4",
            timestamp = timestamp
        )

        verify {
            mockNotificationRepository.notificationIds = setOf(1, 102)
        }
    }
}