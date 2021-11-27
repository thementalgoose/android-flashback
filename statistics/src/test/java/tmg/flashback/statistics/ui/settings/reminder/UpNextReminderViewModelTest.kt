package tmg.flashback.statistics.ui.settings.reminder

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.ui.settings.notifications.reminder.UpNextReminderViewModel
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class UpNextReminderViewModelTest: BaseTest() {

    private val mockScheduleController: ScheduleController = mockk(relaxed = true)

    private lateinit var sut: UpNextReminderViewModel

    private fun initSUT() {
        sut = UpNextReminderViewModel(mockScheduleController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockScheduleController.notificationReminder } returns tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_30
    }

    @Test
    fun `init loads notification list`() {
        initSUT()
        sut.outputs.notificationPrefs.test {
            assertValue(tmg.flashback.statistics.repository.models.NotificationReminder.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it == tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_30
                )
            })
        }
    }

    @Test
    fun `selecting notification reminder updates value in controller`() {
        initSUT()
        sut.inputs.selectNotificationReminder(tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_60)
        verify {
            mockScheduleController.notificationReminder = tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_60
        }
    }

    @Test
    fun `selecting notification reminder updates notifies updated event`() {
        initSUT()
        sut.inputs.selectNotificationReminder(tmg.flashback.statistics.repository.models.NotificationReminder.MINUTES_60)
        sut.outputs.updated.test {
            assertEventFired()
        }
    }
}