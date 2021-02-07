package tmg.flashback.ui.settings.statistics

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.controllers.RaceController
import tmg.flashback.controllers.SeasonController
import tmg.flashback.mockSeason
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test

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
    fun `init with default season clicked shows settings options`() {
        every { mockSeasonController.isUserDefinedValueSet } returns true
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_statistics),
                AppPreferencesItem.SwitchPreference("QualifyingDelta", R.string.settings_customisation_qualifying_delta_title, R.string.settings_customisation_qualifying_delta_description, false),
                AppPreferencesItem.SwitchPreference("QualifyingGridPenalty", R.string.settings_customisation_qualifying_grid_penalties_title, R.string.settings_customisation_qualifying_grid_penalties_description, false),
                AppPreferencesItem.SwitchPreference("FadeOutDNF", R.string.settings_customisation_fade_dnf_title, R.string.settings_customisation_fade_dnf_description, false),
                AppPreferencesItem.Category(R.string.settings_home),
                AppPreferencesItem.Preference("DefaultSeason", R.string.settings_default_season_title, R.string.settings_default_season_description),
                AppPreferencesItem.SwitchPreference("BottomSheetAll", R.string.settings_customisation_season_all_expanded_title, R.string.settings_customisation_season_all_expanded_description, false),
                AppPreferencesItem.SwitchPreference("BottomSheetFavourited", R.string.settings_customisation_season_favourited_expanded_title, R.string.settings_customisation_season_favourited_expanded_description, false)
            ))
        }
    }

    @Test
    fun `init with no default season selected shows settings options`() {
        every { mockSeasonController.isUserDefinedValueSet } returns false
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_statistics),
                AppPreferencesItem.SwitchPreference("QualifyingDelta", R.string.settings_customisation_qualifying_delta_title, R.string.settings_customisation_qualifying_delta_description, false),
                AppPreferencesItem.SwitchPreference("QualifyingGridPenalty", R.string.settings_customisation_qualifying_grid_penalties_title, R.string.settings_customisation_qualifying_grid_penalties_description, false),
                AppPreferencesItem.SwitchPreference("FadeOutDNF", R.string.settings_customisation_fade_dnf_title, R.string.settings_customisation_fade_dnf_description, false),
                AppPreferencesItem.Category(R.string.settings_home),
                AppPreferencesItem.SwitchPreference("BottomSheetAll", R.string.settings_customisation_season_all_expanded_title, R.string.settings_customisation_season_all_expanded_description, false),
                AppPreferencesItem.SwitchPreference("BottomSheetFavourited", R.string.settings_customisation_season_favourited_expanded_title, R.string.settings_customisation_season_favourited_expanded_description, false)
            ))
        }
    }

    @Test
    fun `click qualifying delta toggles option`() {
        initSUT()
        sut.inputs.preferenceClicked("QualifyingDelta", true)
        verify {
            mockRaceController.showQualifyingDelta = true
        }
    }

    @Test
    fun `click qualifying grid penalty toggles option`() {
        initSUT()
        sut.inputs.preferenceClicked("QualifyingGridPenalty", true)
        verify {
            mockRaceController.showGridPenaltiesInQualifying = true
        }
    }

    @Test
    fun `click fade out dnf toggles option`() {
        initSUT()
        sut.inputs.preferenceClicked("FadeOutDNF", true)
        verify {
            mockRaceController.fadeDNF = true
        }
    }

    @Test
    fun `click default season clears default`() {
        initSUT()
        sut.inputs.preferenceClicked("DefaultSeason", null)
        verify {
            mockSeasonController.clearDefault()
        }
        sut.outputs.defaultSeasonChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `click bottom sheet all toggles option`() {
        initSUT()
        sut.inputs.preferenceClicked("BottomSheetAll", true)
        verify {
            mockSeasonController.allExpanded = true
        }
    }

    @Test
    fun `click bottom sheet favourited toggles option`() {
        initSUT()
        sut.inputs.preferenceClicked("BottomSheetFavourited", true)
        verify {
            mockSeasonController.favouritesExpanded = true
        }
    }
}