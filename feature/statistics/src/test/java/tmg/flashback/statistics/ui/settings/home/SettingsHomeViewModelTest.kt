package tmg.flashback.statistics.ui.settings.home

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.HomeController
import tmg.flashback.statistics.usecases.DefaultSeasonUseCase
import tmg.flashback.statistics.testutils.assertExpectedOrder
import tmg.flashback.statistics.testutils.findPref
import tmg.flashback.statistics.testutils.findSwitch
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsHomeViewModelTest: BaseTest() {

    private val mockHomeController: HomeController = mockk(relaxed = true)
    private val mockDefaultSeasonUseCase: DefaultSeasonUseCase = mockk(relaxed = true)

    private lateinit var sut: SettingsHomeViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockDefaultSeasonUseCase.isUserDefinedValueSet } returns false
        every { mockHomeController.allExpanded } returns false
        every { mockHomeController.favouritesExpanded } returns false
    }

    private fun initSUT() {
        sut = SettingsHomeViewModel(mockDefaultSeasonUseCase, mockHomeController)
    }

    @Test
    fun `initial model list is expected when user defined value false`() {
        initSUT()

        val expected = listOf(
            Pair(R.string.settings_home, null),
            Pair(R.string.settings_customisation_season_all_expanded_title, R.string.settings_customisation_season_all_expanded_description),
            Pair(R.string.settings_customisation_season_favourited_expanded_title, R.string.settings_customisation_season_favourited_expanded_description),
            Pair(R.string.settings_information_title, null),
            Pair(R.string.settings_information_at_top_title, R.string.settings_information_at_top_description),
            Pair(R.string.settings_dashboard_title, null),
//            Pair(R.string.settings_dashboard_default_to_schedule_title, R.string.settings_dashboard_default_to_schedule_description),
            Pair(R.string.settings_dashboard_autoscroll_title, R.string.settings_dashboard_autoscroll_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `initial model list is expected when user defined value true`() {
        every { mockDefaultSeasonUseCase.isUserDefinedValueSet } returns true

        initSUT()
        val expected = listOf(
            Pair(R.string.settings_home, null),
            Pair(R.string.settings_default_season_title, R.string.settings_default_season_description),
            Pair(R.string.settings_customisation_season_all_expanded_title, R.string.settings_customisation_season_all_expanded_description),
            Pair(R.string.settings_customisation_season_favourited_expanded_title, R.string.settings_customisation_season_favourited_expanded_description),
            Pair(R.string.settings_information_title, null),
            Pair(R.string.settings_information_at_top_title, R.string.settings_information_at_top_description),
            Pair(R.string.settings_dashboard_title, null),
//            Pair(R.string.settings_dashboard_default_to_schedule_title, R.string.settings_dashboard_default_to_schedule_description),
            Pair(R.string.settings_dashboard_autoscroll_title, R.string.settings_dashboard_autoscroll_description)
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking toggle for clearing default season launches event`() {
        every { mockDefaultSeasonUseCase.isUserDefinedValueSet } returns true
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_default_season_title))
        sut.outputs.defaultSeasonChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking toggle for clearing default season clears toggle`() {
        every { mockDefaultSeasonUseCase.isUserDefinedValueSet } returns true
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_default_season_title))
        verify {
            mockDefaultSeasonUseCase.clearDefault()
        }
    }

    @Test
    fun `clicking toggle for all expanded updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_customisation_season_all_expanded_title), true)
        verify {
            mockHomeController.allExpanded = true
        }
    }

    @Test
    fun `clicking toggle for favourites expanded updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_customisation_season_favourited_expanded_title), true)
        verify {
            mockHomeController.favouritesExpanded = true
        }
    }

    @Test
    fun `clicking toggle for data provided by at top updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_information_at_top_title), true)
        verify {
            mockHomeController.dataProvidedByAtTop = true
        }
    }

//    @Test
//    fun `clicking toggle for dashboard default to schedule by at top updates toggle`() {
//        initSUT()
//        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_dashboard_default_to_schedule_title), true)
//        verify {
//            mockHomeController.dashboardDefaultToSchedule = true
//        }
//    }

    @Test
    fun `clicking toggle for dashboard autoscroll by at top updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_dashboard_autoscroll_title), true)
        verify {
            mockHomeController.dashboardAutoscroll = true
        }
    }
}