package tmg.flashback.statistics.ui.settings

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.ScheduleController
import tmg.flashback.statistics.ui.settings.notifications.UpNextSettingsViewModel
import tmg.flashback.statistics.testutils.assertExpectedOrder
import tmg.flashback.statistics.testutils.findPref
import tmg.flashback.statistics.testutils.findSwitch
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class UpNextSettingsViewModelTest: BaseTest() {

    private val mockScheduleController: ScheduleController = mockk(relaxed = true)

    private lateinit var sut: UpNextSettingsViewModel

    private fun initSUT() {
        sut = UpNextSettingsViewModel(mockScheduleController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockScheduleController.notificationRace } returns true
        every { mockScheduleController.notificationQualifying } returns true
        every { mockScheduleController.notificationFreePractice } returns true
        every { mockScheduleController.notificationSeasonInfo } returns true
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
            Pair(R.string.settings_up_next_time_before_title, R.string.settings_up_next_time_before_description),
            Pair(R.string.settings_up_next_results_available_title, null),
            Pair(R.string.settings_up_next_results_race_title, R.string.settings_up_next_results_race_descrition),
            Pair(R.string.settings_up_next_results_sprint_title, R.string.settings_up_next_results_sprint_descrition),
            Pair(R.string.settings_up_next_results_qualifying_title, R.string.settings_up_next_results_qualifying_descrition)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking toggle for race updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_race_title), true)
        verify {
            mockScheduleController.notificationRace = true
        }
    }

    @Test
    fun `clicking toggle for qualifying updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_qualifying_title), true)
        verify {
            mockScheduleController.notificationQualifying = true
        }
    }

    @Test
    fun `clicking toggle for free practice updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_free_practice_title), true)
        verify {
            mockScheduleController.notificationFreePractice = true
        }
    }

    @Test
    fun `clicking toggle for other updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_category_other_title), true)
        verify {
            mockScheduleController.notificationSeasonInfo = true
        }
    }

    @Test
    fun `clicking toggle for race notify updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_results_qualifying_title), true)
        verify {
            mockScheduleController.notificationQualifyingNotify = true
        }
        coVerify {
            mockScheduleController.resubscribe()
        }
    }

    @Test
    fun `clicking toggle for sprint notify updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_results_sprint_title), true)
        verify {
            mockScheduleController.notificationSprintNotify = true
        }
        coVerify {
            mockScheduleController.resubscribe()
        }
    }

    @Test
    fun `clicking toggle for qualifying notify updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_up_next_results_race_title), true)
        verify {
            mockScheduleController.notificationRaceNotify = true
        }
        coVerify {
            mockScheduleController.resubscribe()
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