package tmg.flashback.upnext.ui.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.upnext.R
import tmg.flashback.upnext.controllers.UpNextController
import tmg.flashback.upnext.testutils.assertExpectedOrder
import tmg.flashback.upnext.testutils.findPref
import tmg.flashback.upnext.testutils.findSwitch
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class UpNextSettingsViewModelTest: BaseTest() {

    private val mockUpNextController: UpNextController = mockk(relaxed = true)

    private lateinit var sut: UpNextSettingsViewModel

    private fun initSUT() {
        sut = UpNextSettingsViewModel(mockUpNextController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockUpNextController.notificationRace } returns true
        every { mockUpNextController.notificationQualifying } returns true
        every { mockUpNextController.notificationFreePractice } returns true
        every { mockUpNextController.notificationSeasonInfo } returns true
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()

        val expected = listOf(
            Pair(R.string.settings_up_next_category_title, null),
            Pair(R.string.settings_up_next_category_race_title, R.string.settings_up_next_category_race_descrition),
            Pair(R.string.settings_up_next_category_qualifying_title, R.string.settings_up_next_category_qualifying_descrition),
            Pair(R.string.settings_up_next_category_free_practice_title, R.string.settings_up_next_category_free_practice_descrition),
            Pair(R.string.settings_up_next_category_other_title, R.string.settings_up_next_category_other_descrition),
            Pair(R.string.settings_up_next_title, null),
            Pair(R.string.settings_up_next_time_before_title, R.string.settings_up_next_time_before_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking toggle for race updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_race_title), true)
        verify {
            mockUpNextController.notificationRace = true
        }
    }

    @Test
    fun `clicking toggle for qualifying updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_qualifying_title), true)
        verify {
            mockUpNextController.notificationQualifying = true
        }
    }

    @Test
    fun `clicking toggle for free practice updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_free_practice_title), true)
        verify {
            mockUpNextController.notificationFreePractice = true
        }
    }

    @Test
    fun `clicking toggle for other updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_other_title), true)
        verify {
            mockUpNextController.notificationSeasonInfo = true
        }
    }

    @Test
    fun `clicking toggle for reminders opens event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_up_next_time_before_title))
        sut.outputs.openTimePicker.test {
            assertEventFired()
        }
    }
}