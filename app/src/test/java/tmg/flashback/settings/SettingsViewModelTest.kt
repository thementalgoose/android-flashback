package tmg.flashback.settings

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.notifications.FirebasePushNotificationManager.Companion.topicRace
import tmg.flashback.repo.toggle.ToggleDB
import tmg.flashback.repo.pref.PrefsDB
import tmg.flashback.repo.enums.BarAnimation.*
import tmg.flashback.repo.enums.ThemePref.*
import tmg.flashback.settings.SettingsOptions.*
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertDataEventValue
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
        whenever(mockToggle.isNotificationChannelsSupported).thenReturn(true)
    }

    private fun initSUT() {

        sut = SettingsViewModel(mockPrefs, mockToggle, testScopeProvider)
    }

    /**
     * Expected news list that should be displayed in the SettingsViewModel
     */
    private fun expectedNewsList(isChannelsSupported: Boolean = true): List<AppPreferencesItem> {
        return mutableListOf<AppPreferencesItem>().apply {
            add(AppPreferencesItem.Category(R.string.settings_customisation_rss))
            add(NEWS.toPref())
            add(AppPreferencesItem.Category(R.string.settings_notifications_title))
            if (isChannelsSupported) {
                add(NOTIFICATIONS_CHANNEL_QUALIFYING.toPref())
                add(NOTIFICATIONS_CHANNEL_RACE.toPref())
            }
            else {
                add(NOTIFICATIONS_SETTINGS.toPref())
            }
            add(AppPreferencesItem.Category(R.string.settings_theme))
            add(THEME.toPref())
            add(AppPreferencesItem.Category(R.string.settings_customisation))
            add(BAR_ANIMATION_SPEED.toPref())
            add(QUALIFYING_DELTAS.toSwitch(false))
            add(QUALIFYING_GRID_PENALTY.toSwitch(false))
            add(AppPreferencesItem.Category(R.string.settings_season_list))
            add(SEASON_BOTTOM_SHEET_EXPANDED.toSwitch(false))
            add(SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(false))
            add(SEASON_BOTTOM_SHEET_ALL.toSwitch(false))
            add(AppPreferencesItem.Category(R.string.settings_help))
            add(ABOUT.toPref())
            add(PRIVACY_POLICY.toPref())
            add(RELEASE.toPref())
            add(AppPreferencesItem.Category(R.string.settings_feedback))
            add(CRASH.toSwitch(false))
            add(SUGGESTION.toPref())
            add(SHAKE.toSwitch(false))
        }
    }


    @Test
    fun `SettingsViewModel setup populates settings list`() {

        initSUT()

        sut.outputs.settings.test {
            assertValue(expectedNewsList())
        }
    }

    @Test
    fun `SettingsViewModel setup with notification channels not supported doesnt show individual options`() {

        whenever(mockToggle.isNotificationChannelsSupported).thenReturn(false)

        initSUT()

        sut.outputs.settings.test {
            assertValue(expectedNewsList(false))
        }
    }

    @Test
    fun `SettingsViewModel setup populates settings list with toggle disabled hides the news`() {

        whenever(mockToggle.isRSSEnabled).thenReturn(false)

        initSUT()

        // Filter out the news items we are expecting (should mean test still passes if order changes)
        val expected = expectedNewsList().filter {
            when (it) {
                is AppPreferencesItem.Category -> it.title != R.string.settings_customisation_rss
                is AppPreferencesItem.Preference -> it.prefKey != "news"
                else -> true
            }
        }

        sut.outputs.settings.test {
            assertValue(expected)
        }
    }

    @Test
    fun `SettingsViewModel setup populates settings theme list`() {

        initSUT()

        val expected = listOf(
                Selected(BottomSheetItem(DAY.ordinal, DAY.icon, DAY.label), false),
                Selected(BottomSheetItem(AUTO.ordinal, AUTO.icon, AUTO.label), true),
                Selected(BottomSheetItem(NIGHT.ordinal, NIGHT.icon, NIGHT.label), false)
        )

        sut.outputs.themePreferences.test {
            assertValue(expected)
        }
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

        sut.outputs.animationPreference.test {
            assertValue(expected)
        }
    }

    @Test
    fun `SettingsViewModel select a theme updates the pref`() {

        initSUT()

        sut.pickTheme(NIGHT)
        verify(mockPrefs).theme = NIGHT

        sut.outputs.themeChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel select a bar animation updates the pref`() {

        initSUT()

        sut.pickAnimationSpeed(SLOW)
        verify(mockPrefs).barAnimation = SLOW

        sut.outputs.animationChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel selecting theme pref opens picker`() {

        initSUT()

        sut.inputs.preferenceClicked(SettingsOptions.THEME, null)

        sut.outputs.openThemePicker.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel selecting animation pref opens picker`() {

        initSUT()

        sut.inputs.preferenceClicked(BAR_ANIMATION_SPEED, null)

        sut.outputs.openAnimationPicker.test {
            assertEventFired()
        }
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

        sut.outputs.openAbout.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel selecting release fires open release notes event`() {

        initSUT()

        sut.inputs.preferenceClicked(RELEASE, null)

        sut.outputs.openRelease.test {
            assertEventFired()
        }
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

        sut.outputs.openSuggestions.test {
            assertEventFired()
        }
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

        sut.outputs.openNews.test {
            assertEventFired()
        }
    }

    @Test
    fun `SettingsViewModel selecting notification qualifying channel fires notification event`() {

        initSUT()

        sut.inputs.preferenceClicked(NOTIFICATIONS_CHANNEL_QUALIFYING, null)

        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue(topicQualifying)
        }
    }

    @Test
    fun `SettingsViewModel selecting notification race channel fires notification event`() {

        initSUT()

        sut.inputs.preferenceClicked(NOTIFICATIONS_CHANNEL_RACE, null)

        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue(topicRace)
        }
    }

    @Test
    fun `SettingsViewModel selecting notification settings fires notification event`() {

        initSUT()

        sut.inputs.preferenceClicked(NOTIFICATIONS_SETTINGS, null)

        sut.outputs.openNotifications.test {
            assertEventFired()
        }
    }

    @AfterEach
    internal fun tearDown() {
        reset(mockPrefs)
    }
}