package tmg.flashback.ui.settings.notifications

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.results.repository.NotificationsRepositoryImpl
import tmg.flashback.results.repository.models.NotificationReminder
import tmg.flashback.results.usecases.ScheduleNotificationsUseCase
import tmg.flashback.ui.settings.Settings
import tmg.testutils.BaseTest

internal class SettingsNotificationsUpcomingNoticeViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationsRepositoryImpl = mockk(relaxed = true)
    private val mockScheduleNotificationsUseCase: ScheduleNotificationsUseCase = mockk(relaxed = true)

    private lateinit var sut: SettingsNotificationsUpcomingNoticeViewModel

    private fun initSUT() {
        sut = SettingsNotificationsUpcomingNoticeViewModel(
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
            Assertions.assertEquals(NotificationReminder.MINUTES_30, awaitItem())
        }
    }

    @Test
    fun `selecting notification reminder updates value in controller`() {
        initSUT()
        sut.inputs.prefClicked(Settings.Notifications.notificationNoticePeriod(NotificationReminder.MINUTES_60, true))
        verify {
            mockNotificationRepository.notificationReminderPeriod = NotificationReminder.MINUTES_60
            mockScheduleNotificationsUseCase.schedule()
        }
    }
}