package tmg.flashback.settings

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.ThemePref.*
import tmg.flashback.settings.SettingsOptions.*
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.test
import tmg.flashback.utils.Selected
import tmg.flashback.utils.bottomsheet.BottomSheetItem

class SettingsViewModelTest: BaseTest() {

    lateinit var sut: SettingsViewModel

    private val mockPrefs: PrefsDB = mock()

    @BeforeEach
    fun setUp() {

        whenever(mockPrefs.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefs.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockPrefs.showDriversBehindConstructor).thenReturn(false)
        whenever(mockPrefs.showBottomSheetExpanded).thenReturn(false)
        whenever(mockPrefs.showBottomSheetFavourited).thenReturn(false)
        whenever(mockPrefs.showBottomSheetAll).thenReturn(false)
        whenever(mockPrefs.crashReporting).thenReturn(false)
        whenever(mockPrefs.shakeToReport).thenReturn(false)

        whenever(mockPrefs.theme).thenReturn(AUTO)

        sut = SettingsViewModel(mockPrefs)

    }

    @Test
    fun `SettingsViewModel setup populates settings list`() {

        val expected: List<AppPreferencesItem> = listOf(
                AppPreferencesItem.Category(R.string.settings_customisation),
                QUALIFYING_DELTAS.toSwitch(false),
                QUALIFYING_GRID_PENALTY.toSwitch(false),
                SHOW_DRIVERS_POINTS_IN_CONSTRUCTORS.toSwitch(false),
                AppPreferencesItem.Category(R.string.settings_theme),
                THEME.toPref(),
                AppPreferencesItem.Category(R.string.settings_customisation_news),
                NEWS.toPref(),
                AppPreferencesItem.Category(R.string.settings_season_list),
                SEASON_BOTTOM_SHEET_EXPANDED.toSwitch(false),
                SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(false),
                SEASON_BOTTOM_SHEET_ALL.toSwitch(false),
                AppPreferencesItem.Category(R.string.settings_help),
                ABOUT.toPref(),
                RELEASE.toPref(),
                AppPreferencesItem.Category(R.string.settings_feedback),
                CRASH.toSwitch(false),
                SUGGESTION.toPref(),
                SHAKE.toSwitch(false)
        )

        assertEquals(expected, sut.settings.test().latestValue())
    }

    @Test
    fun `SettingsViewModel setup populates settings theme list`() {

        val expected = listOf(
            Selected(BottomSheetItem(DAY.ordinal, DAY.icon, DAY.label), false),
            Selected(BottomSheetItem(AUTO.ordinal, AUTO.icon, AUTO.label), true),
            Selected(BottomSheetItem(NIGHT.ordinal, NIGHT.icon, NIGHT.label), false)
        )

        assertEquals(expected, sut.themePreferences.test().latestValue())
    }

    @Test
    fun `SettingsViewModel select a theme updates the pref`() {

        sut.pickTheme(NIGHT)
        verify(mockPrefs).theme = NIGHT

        sut.themeChanged.test().assertEventFired()
    }

    @Test
    fun `SettingsViewModel selecting theme pref opens picker`() {

        sut.inputs.preferenceClicked(SettingsOptions.THEME, null)

        sut.openThemePicker.test().assertEventFired()
    }

    @Test
    fun `SettingsViewModel selecting qualifying delta pref updates value`() {

        sut.inputs.preferenceClicked(QUALIFYING_DELTAS, true)

        verify(mockPrefs).showQualifyingDelta = true
    }

    @Test
    fun `SettingsViewModel selecting qualifying grid penalty updates value`() {

        sut.inputs.preferenceClicked(QUALIFYING_GRID_PENALTY, true)

        verify(mockPrefs).showGridPenaltiesInQualifying = true
    }

    @Test
    fun `SettingsViewModel selecting show drivers in constructor updates value`() {

        sut.inputs.preferenceClicked(SHOW_DRIVERS_POINTS_IN_CONSTRUCTORS, true)

        verify(mockPrefs).showDriversBehindConstructor = true
    }

    @Test
    fun `SettingsViewModel selecting season bottom sheet expand updates value`() {

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_EXPANDED, true)

        verify(mockPrefs).showBottomSheetExpanded = true
    }

    @Test
    fun `SettingsViewModel selecting season bottom sheet favourited updates value`() {

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_FAVOURITED, true)

        verify(mockPrefs).showBottomSheetFavourited = true
    }

    @Test
    fun `SettingsViewModel selecting season bottom sheet all updates value`() {

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_ALL, true)

        verify(mockPrefs).showBottomSheetAll = true
    }

    @Test
    fun `SettingsViewModel selecting about fires open about event`() {

        sut.inputs.preferenceClicked(ABOUT, null)

        sut.outputs.openAbout.test().assertEventFired()
    }

    @Test
    fun `SettingsViewModel selecting release fires open release notes event`() {

        sut.inputs.preferenceClicked(RELEASE, null)

        sut.outputs.openRelease.test().assertEventFired()
    }

    @Test
    fun `SettingsViewModel selecting crash reporting updates value`() {

        sut.inputs.preferenceClicked(CRASH, true)

        verify(mockPrefs).crashReporting = true
    }

    @Test
    fun `SettingsViewModel selecting suggestions fires open suggestions event`() {

        sut.inputs.preferenceClicked(SUGGESTION, null)

        sut.outputs.openSuggestions.test().assertEventFired()
    }

    @Test
    fun `SettingsViewModel selecting shake to report updates value`() {

        sut.inputs.preferenceClicked(SHAKE, true)

        verify(mockPrefs).shakeToReport = true
    }

    @Test
    fun `SettingsViewModel selecting news fires open news settings event`() {

        sut.inputs.preferenceClicked(NEWS, null)

        sut.outputs.openNews.test().assertEventFired()
    }


    @AfterEach
    internal fun tearDown() {
        reset(mockPrefs)
    }
}