package tmg.flashback.statistics.ui.settings.statistics

import androidx.annotation.StringRes
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.ui.settings.SettingsModel
import tmg.flashback.statistics.R
import tmg.flashback.statistics.controllers.RaceController
import tmg.flashback.statistics.controllers.SeasonController
import tmg.flashback.statistics.testutils.findPref
import tmg.flashback.statistics.testutils.findSwitch
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsStatisticsViewModelTest: BaseTest() {

    private val mockRaceController: RaceController = mockk(relaxed = true)
    private val mockSeasonController: SeasonController = mockk(relaxed = true)

    private lateinit var sut: SettingsStatisticsViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockRaceController.showQualifyingDelta } returns false
        every { mockRaceController.showGridPenaltiesInQualifying } returns false
        every { mockRaceController.fadeDNF } returns false
        every { mockSeasonController.isUserDefinedValueSet } returns false
        every { mockSeasonController.allExpanded } returns false
        every { mockSeasonController.favouritesExpanded } returns false
    }

    private fun initSUT() {
        sut = SettingsStatisticsViewModel(mockRaceController, mockSeasonController)
    }

    @Test
    fun `initial model list is expected when user defined value false`() {
        initSUT()

        val expected = listOf(
                Pair(R.string.settings_statistics, null),
                Pair(R.string.settings_customisation_qualifying_delta_title, R.string.settings_customisation_qualifying_delta_description),
                Pair(R.string.settings_customisation_qualifying_grid_penalties_title, R.string.settings_customisation_qualifying_grid_penalties_description),
                Pair(R.string.settings_customisation_fade_dnf_title, R.string.settings_customisation_fade_dnf_description),
                Pair(R.string.settings_home, null),
                Pair(R.string.settings_customisation_season_all_expanded_title, R.string.settings_customisation_season_all_expanded_description),
                Pair(R.string.settings_customisation_season_favourited_expanded_title, R.string.settings_customisation_season_favourited_expanded_description),
        )

        sut.models.forEachIndexed { index, settingsModel ->
            if (settingsModel is SettingsModel.Header) {
                assertEquals(expected[index].first, settingsModel.title)
            }
            if (settingsModel is SettingsModel.SwitchPref) {
                assertEquals(expected[index].first, settingsModel.title)
                assertEquals(expected[index].second, settingsModel.description)
            }
            if (settingsModel is SettingsModel.Pref) {
                assertEquals(expected[index].first, settingsModel.title)
                assertEquals(expected[index].second, settingsModel.description)
            }
        }
    }

    @Test
    fun `initial model list is expected when user defined value true`() {
        every { mockSeasonController.isUserDefinedValueSet } returns true

        initSUT()
        val expected = listOf(
                Pair(R.string.settings_statistics, null),
                Pair(R.string.settings_customisation_qualifying_delta_title, R.string.settings_customisation_qualifying_delta_description),
                Pair(R.string.settings_customisation_qualifying_grid_penalties_title, R.string.settings_customisation_qualifying_grid_penalties_description),
                Pair(R.string.settings_customisation_fade_dnf_title, R.string.settings_customisation_fade_dnf_description),
                Pair(R.string.settings_home, null),
                Pair(R.string.settings_default_season_title, R.string.settings_default_season_description),
                Pair(R.string.settings_customisation_season_all_expanded_title, R.string.settings_customisation_season_all_expanded_description),
                Pair(R.string.settings_customisation_season_favourited_expanded_title, R.string.settings_customisation_season_favourited_expanded_description),
        )

        sut.models.forEachIndexed { index, settingsModel ->
            if (settingsModel is SettingsModel.Header) {
                assertEquals(expected[index].first, settingsModel.title)
            }
            if (settingsModel is SettingsModel.SwitchPref) {
                assertEquals(expected[index].first, settingsModel.title)
                assertEquals(expected[index].second, settingsModel.description)
            }
            if (settingsModel is SettingsModel.Pref) {
                assertEquals(expected[index].first, settingsModel.title)
                assertEquals(expected[index].second, settingsModel.description)
            }
        }
    }

    @Test
    fun `clicking toggle for qualifying delta updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_customisation_qualifying_delta_title), true)
        verify {
            mockRaceController.showQualifyingDelta
            mockRaceController.showQualifyingDelta = true
        }
    }

    @Test
    fun `clicking toggle for qualifying grid penalties updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_customisation_qualifying_grid_penalties_title), true)
        verify {
            mockRaceController.showGridPenaltiesInQualifying
            mockRaceController.showGridPenaltiesInQualifying = true
        }
    }

    @Test
    fun `clicking toggle for fade dnf updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_customisation_fade_dnf_title), true)
        verify {
            mockRaceController.fadeDNF
            mockRaceController.fadeDNF = true
        }
    }

    @Test
    fun `clicking toggle for clearing default season launches event`() {
        every { mockSeasonController.isUserDefinedValueSet } returns true
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_default_season_title))
        sut.outputs.defaultSeasonChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking toggle for clearing default season clears toggle`() {
        every { mockSeasonController.isUserDefinedValueSet } returns true
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_default_season_title))
        verify {
            mockSeasonController.clearDefault()
        }
    }

    @Test
    fun `clicking toggle for all expanded updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_customisation_season_all_expanded_title), true)
        verify {
            mockSeasonController.allExpanded
            mockSeasonController.allExpanded = true
        }
    }

    @Test
    fun `clicking toggle for favourites expanded updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models.findSwitch(R.string.settings_customisation_season_favourited_expanded_description), true)
        verify {
            mockSeasonController.favouritesExpanded
            mockSeasonController.favouritesExpanded = true
        }
    }
}