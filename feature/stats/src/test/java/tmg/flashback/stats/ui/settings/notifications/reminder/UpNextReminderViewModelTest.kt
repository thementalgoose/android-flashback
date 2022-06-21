package tmg.flashback.stats.ui.settings.notifications.reminder

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.stats.repository.NotificationRepository
import tmg.flashback.stats.repository.models.NotificationReminder
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder


internal class UpNextReminderViewModelTest: BaseTest() {

    private val mockNotificationRepository: NotificationRepository = mockk(relaxed = true)

    private lateinit var sut: UpNextReminderViewModel

    private fun initSUT() {
        sut = UpNextReminderViewModel(mockNotificationRepository)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockNotificationRepository.notificationReminderPeriod } returns NotificationReminder.MINUTES_30
    }

    @Test
    fun `init loads notification list`() {
        initSUT()
        sut.outputs.notificationPrefs.test {
            assertValue(NotificationReminder.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it == NotificationReminder.MINUTES_30
                )
            })
        }
    }

    @Test
    fun `selecting notification reminder updates value in controller`() {
        initSUT()
        sut.inputs.selectNotificationReminder(NotificationReminder.MINUTES_60)
        verify {
            mockNotificationRepository.notificationReminderPeriod = NotificationReminder.MINUTES_60
        }
    }

    @Test
    fun `selecting notification reminder updates notifies updated event`() {
        initSUT()
        sut.inputs.selectNotificationReminder(NotificationReminder.MINUTES_60)
        sut.outputs.updated.test {
            assertEventFired()
        }
    }
}