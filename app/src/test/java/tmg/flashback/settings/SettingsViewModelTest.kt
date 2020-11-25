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
import tmg.flashback.repo.ToggleDB
import tmg.flashback.repo.db.PrefsDB
import tmg.flashback.repo.enums.BarAnimation.*
import tmg.flashback.repo.enums.ThemePref.*
import tmg.flashback.settings.SettingsOptions.*
import tmg.flashback.rss.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test
import tmg.flashback.utils.Selected
import tmg.flashback.utils.bottomsheet.BottomSheetItem

class SettingsViewModelTest: BaseTest() {

    lateinit var sut: SettingsViewModel

    private val mockPrefs: PrefsDB = mock()
    private val mockToggle: ToggleDB = mock()

    @BeforeEach
    internal fun setUp() {

        whenever(mockPrefs.showQualifyingDelta).thenReturn(false)
        whenever(mockPrefs.showGridPenaltiesInQualifying).thenReturn(false)
        whenever(mockPrefs.showBottomSheetExpanded).thenReturn(false)
        whenever(mockPrefs.showBottomSheetFavourited).thenReturn(false)
        whenever(mockPrefs.showBottomSheetAll).thenReturn(false)
        whenever(mockPrefs.crashReporting).thenReturn(false)
        whenever(mockPrefs.shakeToReport).thenReturn(false)

        whenever(mockPrefs.theme).thenReturn(AUTO)
        whenever(mockPrefs.barAnimation).thenReturn(MEDIUM)

        whenever(mockToggle.isRSSEnabled).thenReturn(true)
    }

    private fun initSUT() {

        sut = SettingsViewModel(mockPrefs, mockToggle, testScopeProvider)
    }

    /**
     * Expected news list that should be displayed in the SettingsViewModel
     */
    private val expectedNewsList: List<AppPreferencesItem> = listOf(
        AppPreferencesItem.Category(R.string.settings_customisation_rss),
        NEWS.toPref(),
        AppPreferencesItem.Category(R.string.settings_theme),
        THEME.toPref(),
        AppPreferencesItem.Category(R.string.settings_customisation),
        BAR_ANIMATION_SPEED.toPref(),
        QUALIFYING_DELTAS.toSwitch(false),
        QUALIFYING_GRID_PENALTY.toSwitch(false),
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


    @Test
    fun `SettingsViewModel setup populates settings list`() {

        initSUT()

        assertEquals(expectedNewsList, sut.settings.test().latestValue())
    }

    @Test
    fun `SettingsViewModel setup populates settings list with toggle disabled hides the news`() {

        whenever(mockToggle.isRSSEnabled).thenReturn(false)

        initSUT()

        // Filter out the news items we are expecting (should mean test still passes if order changes)
        val expected = expectedNewsList.filter {
            when (it) {
                is AppPreferencesItem.Category -> it.title != R.string.settings_customisation_rss
                is AppPreferencesItem.Preference -> it.prefKey != "news"
                else -> true
            }
        }

        assertEquals(expected, sut.settings.test().latestValue())
    }

    @Test
    fun `SettingsViewModel setup populates settings theme list`() {

        initSUT()

        val expected = listOf(
                Selected(BottomSheetItem(DAY.ordinal, DAY.icon, DAY.label), false),
                Selected(BottomSheetItem(AUTO.ordinal, AUTO.icon, AUTO.label), true),
                Selected(BottomSheetItem(NIGHT.ordinal, NIGHT.icon, NIGHT.label), false)
        )

        assertEquals(expected, sut.themePreferences.test().latestValue())
    }

    @Test
    fun `SettingsViewModel setup populates settings animation list`() {

        initSUT()

        val expected = listOf(
                Selected(BottomSheetItem(NONE.ordinal, NONE.icon, NONE.label), false),
                Selected(BottomSheetItem(QUICK.ordinal, QUICK.icon, QUICK.label), false),
                Selected(BottomSheetItem(MEDIUM.ordinal, MEDIUM.icon, MEDIUM.label), true),
                Selected(BottomSheetItem(SLOW.ordinal, SLOW.icon, SLOW.label), false)
        )

        assertEquals(expected, sut.animationPreference.test().latestValue())
    }

    @Test
    fun `SettingsViewModel select a theme updates the pref`() {

        initSUT()

        sut.pickTheme(NIGHT)
        verify(mockPrefs).theme = NIGHT

        assertEventFired(sut.outputs.themeChanged)
    }

    @Test
    fun `SettingsViewModel select a bar animation updates the pref`() {

        initSUT()

        sut.pickAnimationSpeed(SLOW)
        verify(mockPrefs).barAnimation = SLOW

        assertEventFired(sut.outputs.animationChanged)
    }

    @Test
    fun `SettingsViewModel selecting theme pref opens picker`() {

        initSUT()

        sut.inputs.preferenceClicked(SettingsOptions.THEME, null)

        assertEventFired(sut.openThemePicker)
    }

    @Test
    fun `SettingsViewModel selecting animation pref opens picker`() {

        initSUT()

        sut.inputs.preferenceClicked(BAR_ANIMATION_SPEED, null)

        assertEventFired(sut.openAnimationPicker)
    }

    @Test
    fun `SettingsViewModel selecting qualifying delta pref updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(QUALIFYING_DELTAS, true)

        verify(mockPrefs).showQualifyingDelta = true
    }

    @Test
    fun `SettingsViewModel selecting qualifying grid penalty updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(QUALIFYING_GRID_PENALTY, true)

        verify(mockPrefs).showGridPenaltiesInQualifying = true
    }

    @Test
    fun `SettingsViewModel selecting season bottom sheet expand updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_EXPANDED, true)

        verify(mockPrefs).showBottomSheetExpanded = true
    }

    @Test
    fun `SettingsViewModel selecting season bottom sheet favourited updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_FAVOURITED, true)

        verify(mockPrefs).showBottomSheetFavourited = true
    }

    @Test
    fun `SettingsViewModel selecting season bottom sheet all updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_ALL, true)

        verify(mockPrefs).showBottomSheetAll = true
    }

    @Test
    fun `SettingsViewModel selecting about fires open about event`() {

        initSUT()

        sut.inputs.preferenceClicked(ABOUT, null)

        assertEventFired(sut.outputs.openAbout)
    }

    @Test
    fun `SettingsViewModel selecting release fires open release notes event`() {

        initSUT()

        sut.inputs.preferenceClicked(RELEASE, null)

        assertEventFired(sut.outputs.openRelease)
    }

    @Test
    fun `SettingsViewModel selecting crash reporting updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(CRASH, true)

        verify(mockPrefs).crashReporting = true
    }

    @Test
    fun `SettingsViewModel selecting suggestions fires open suggestions event`() {

        initSUT()

        sut.inputs.preferenceClicked(SUGGESTION, null)

        assertEventFired(sut.outputs.openSuggestions)
    }

    @Test
    fun `SettingsViewModel selecting shake to report updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(SHAKE, true)

        verify(mockPrefs).shakeToReport = true
    }

    @Test
    fun `SettingsViewModel selecting news fires open news settings event`() {

        initSUT()

        sut.inputs.preferenceClicked(NEWS, null)

        assertEventFired(sut.outputs.openNews)
    }

    @AfterEach
    internal fun tearDown() {
        reset(mockPrefs)
    }
}