package tmg.flashback.statistics.ui.settings.statistics

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
        (sut.models[0] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_statistics, this.title)
        }
        (sut.models[1] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_qualifying_delta_title, this.title)
            assertEquals(R.string.settings_customisation_qualifying_delta_description, this.description)
        }
        (sut.models[2] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_qualifying_grid_penalties_title, this.title)
            assertEquals(R.string.settings_customisation_qualifying_grid_penalties_description, this.description)
        }
        (sut.models[3] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_fade_dnf_title, this.title)
            assertEquals(R.string.settings_customisation_fade_dnf_description, this.description)
        }
        (sut.models[4] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_home, this.title)
        }
        (sut.models[5] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_season_all_expanded_title, this.title)
            assertEquals(R.string.settings_customisation_season_all_expanded_description, this.description)
        }
        (sut.models[6] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_season_favourited_expanded_title, this.title)
            assertEquals(R.string.settings_customisation_season_favourited_expanded_title, this.description)
        }
    }

    @Test
    fun `initial model list is expected when user defined value true`() {
        every { mockSeasonController.isUserDefinedValueSet } returns true
        initSUT()
        (sut.models[0] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_statistics, this.title)
        }
        (sut.models[1] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_qualifying_delta_title, this.title)
            assertEquals(R.string.settings_customisation_qualifying_delta_description, this.description)
        }
        (sut.models[2] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_qualifying_grid_penalties_title, this.title)
            assertEquals(R.string.settings_customisation_qualifying_grid_penalties_description, this.description)
        }
        (sut.models[3] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_fade_dnf_title, this.title)
            assertEquals(R.string.settings_customisation_fade_dnf_description, this.description)
        }
        (sut.models[4] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_home, this.title)
        }
        (sut.models[5] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_default_season_title, this.title)
            assertEquals(R.string.settings_default_season_description, this.description)
        }
        (sut.models[6] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_season_all_expanded_title, this.title)
            assertEquals(R.string.settings_customisation_season_all_expanded_description, this.description)
        }
        (sut.models[7] as SettingsModel.SwitchPref).apply {
            assertEquals(R.string.settings_customisation_season_favourited_expanded_title, this.title)
            assertEquals(R.string.settings_customisation_season_favourited_expanded_title, this.description)
        }
    }

    @Test
    fun `clicking toggle for qualifying delta updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[1] as SettingsModel.SwitchPref, true)
        verify {
            mockRaceController.showQualifyingDelta = true
        }
    }

    @Test
    fun `clicking toggle for qualifying grid penalties updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[2] as SettingsModel.SwitchPref, true)
        verify {
            mockRaceController.showGridPenaltiesInQualifying= true
        }
    }

    @Test
    fun `clicking toggle for fade dnf updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[3] as SettingsModel.SwitchPref, true)
        verify {
            mockRaceController.fadeDNF = true
        }
    }

    @Test
    fun `clicking toggle for clearing default season launches event`() {
        every { mockSeasonController.isUserDefinedValueSet } returns true
        initSUT()
        sut.clickPreference(sut.models[5] as SettingsModel.Pref)
        sut.outputs.defaultSeasonChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking toggle for clearing default season clears toggle`() {
        every { mockSeasonController.isUserDefinedValueSet } returns true
        initSUT()
        sut.clickPreference(sut.models[5] as SettingsModel.Pref)
        verify {
            mockSeasonController.clearDefault()
        }
    }

    @Test
    fun `clicking toggle for all expanded updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[5] as SettingsModel.SwitchPref, true)
        verify {
            mockSeasonController.allExpanded = true
        }
    }

    @Test
    fun `clicking toggle for favourites expanded updates toggle`() {
        initSUT()
        sut.clickSwitchPreference(sut.models[6] as SettingsModel.SwitchPref, true)
        verify {
            mockSeasonController.favouritesExpanded = true
        }
    }
}