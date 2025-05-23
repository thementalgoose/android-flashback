package tmg.flashback.notifications.usecases

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.notifications.managers.SystemAlarmManager
import tmg.flashback.notifications.repository.NotificationIdsRepository

internal class LocalNotificationCancelUseCaseTest {

    private val mockNotificationIdsRepository: NotificationIdsRepository = mockk(relaxed = true)
    private val mockAlarmManager: SystemAlarmManager = mockk(relaxed = true)

    private lateinit var underTest: LocalNotificationCancelUseCase

    private fun initUnderTest() {
        underTest = LocalNotificationCancelUseCase(
            mockNotificationIdsRepository,
            mockAlarmManager
        )
    }

    @Test
    fun `cancelling specific notification sends cancel to alarm manager`() {
        initUnderTest()
        underTest.cancel(101)

        verify {
            mockAlarmManager.cancel(101)
        }
    }

    @Test
    fun `cancelling specific notification removes notification id from stored ids`() {
        every { mockNotificationIdsRepository.notificationIds } returns setOf(101, 102, 103)

        initUnderTest()
        underTest.cancel(101)

        verify {
            mockAlarmManager.cancel(101)
            mockNotificationIdsRepository.notificationIds = setOf(102, 103)
        }
    }

    @Test
    fun `cancelling all notification removes notification id from stored ids`() {
        every { mockNotificationIdsRepository.notificationIds } returns setOf(101, 102, 103)

        initUnderTest()
        underTest.cancelAll()

        verify {
            mockAlarmManager.cancel(101)
            mockAlarmManager.cancel(102)
            mockAlarmManager.cancel(103)
            mockNotificationIdsRepository.notificationIds = emptySet()
        }
    }
}