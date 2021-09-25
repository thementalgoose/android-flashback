package tmg.flashback.upnext.ui.settings.reminder

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.model.NotificationReminder
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class UpNextReminderViewModelTest: BaseTest() {

    private val mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: UpNextReminderViewModel

    private fun initSUT() {
        sut = UpNextReminderViewModel(mockUpNextController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextController.notificationReminder } returns NotificationReminder.MINUTES_30
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
            mockUpNextController.notificationReminder = NotificationReminder.MINUTES_60
        }
    }
}