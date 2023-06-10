package tmg.flashback.results.ui.settings.notifications.reminder

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test


internal class UpNextReminderViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)

    private lateinit var sut: UpNextReminderViewModel

    private fun initSUT() {
        sut = UpNextReminderViewModel(
            notificationRepository = mockNotificationRepository,
            scheduleNotificationsUseCase = mockScheduleNotificationsUseCase
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30
    }

    @Test
    fun `init loads notification list`() = runTest {
        initSUT()
        sut.outputs.currentlySelected.test {
            assertEquals(NotificationReminder.MINUTES_30, awaitItem())
        }
    }

    @Test
    fun `selecting notification reminder updates value in controller`() {
        initSUT()
        sut.inputs.selectNotificationReminder(NotificationReminder.MINUTES_60)
        verify {
            mockNotificationRepository.notificationReminderPeriod = NotificationReminder.MINUTES_60
            mockScheduleNotificationsUseCase.schedule()
        }
    }
}