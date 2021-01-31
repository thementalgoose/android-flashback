package tmg.flashback.ui.settings

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.BuildConfig
import tmg.flashback.R
import tmg.flashback.controllers.*
import tmg.flashback.core.controllers.AnalyticsController
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.controllers.CrashController
import tmg.flashback.core.controllers.DeviceController
import tmg.flashback.core.enums.AnimationSpeed.*
import tmg.flashback.core.enums.Theme.*
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.managers.notifications.FirebasePushNotificationManager.Companion.topicQualifying
import tmg.flashback.managers.notifications.FirebasePushNotificationManager.Companion.topicRace
import tmg.flashback.ui.settings.SettingsOptions.*
import tmg.flashback.testutils.*
import tmg.flashback.testutils.assertDataEventValue
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test
import tmg.flashback.ui.utils.Selected
import tmg.flashback.ui.utils.StringHolder
import tmg.flashback.ui.utils.bottomsheet.BottomSheetItem

internal class SettingsViewModelTest: BaseTest() {

    lateinit var sut: SettingsViewModel

    private val notificationController: NotificationController = mockk(relaxed = true)
    private val appearanceController: AppearanceController = mockk(relaxed = true)
    private val deviceController: DeviceController = mockk(relaxed = true)
    private val raceController: RaceController = mockk(relaxed = true)
    private val crashController: CrashController = mockk(relaxed = true)
    private val analyticsController: AnalyticsController = mockk(relaxed = true)
    private val seasonController: SeasonController = mockk(relaxed = true)
    private val featureController: FeatureController = mockk(relaxed = true)

    @BeforeEach
    internal fun setUp() {

        every { raceController.showQualifyingDelta } returns false
        every { raceController.showGridPenaltiesInQualifying } returns false
        every { raceController.fadeDNF } returns false
        every { seasonController.favouritesExpanded } returns false
        every { seasonController.allExpanded } returns false
        every { crashController.enabled } returns false
        every { analyticsController.enabled } returns false
        every { deviceController.shakeToReport } returns false
        every { notificationController.isNotificationChannelsSupported } returns true

        every { appearanceController.currentTheme } returns AUTO
        every { appearanceController.animationSpeed } returns MEDIUM

        every { featureController.rssEnabled } returns true
    }

    private fun initSUT() {

        sut = SettingsViewModel(
                notificationController,
                appearanceController,
                deviceController,
                raceController,
                analyticsController,
                crashController,
                seasonController,
                featureController
        )
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
            add(ANIMATION_SPEED.toPref())
            add(QUALIFYING_DELTAS.toSwitch(false))
            add(FADE_OUT_DNF.toSwitch(false))
            add(QUALIFYING_GRID_PENALTY.toSwitch(false))
            add(AppPreferencesItem.Category(R.string.settings_season_list))
            add(DEFAULT_SEASON.toPref())
            add(SEASON_BOTTOM_SHEET_FAVOURITED.toSwitch(false))
            add(SEASON_BOTTOM_SHEET_ALL.toSwitch(false))
            add(AppPreferencesItem.Category(R.string.settings_widgets))
            add(WIDGETS_REFRESH_ALL.toPref())
            add(AppPreferencesItem.Category(R.string.settings_help))
            add(ABOUT.toPref())
            add(REVIEW.toPref())
            add(PRIVACY_POLICY.toPref())
            add(RELEASE.toPref())
            add(AppPreferencesItem.Category(R.string.settings_feedback))
            add(CRASH.toSwitch(false))
            add(ANALYTICS.toSwitch(false))
            add(SUGGESTION.toPref())
            add(SHAKE.toSwitch(false))
        }
    }


    @Test
    fun `setup populates settings list`() {

        initSUT()

        sut.outputs.settings.test {
            assertValue(expectedNewsList())
        }
    }

    @Test
    fun `setup with notification channels not supported doesnt show individual options`() {

        every { notificationController.isNotificationChannelsSupported } returns false

        initSUT()

        sut.outputs.settings.test {
            assertValue(expectedNewsList(false))
        }
    }

    @Test
    fun `setup populates settings list with toggle disabled hides the news`() {

        every { featureController.rssEnabled } returns false

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
    fun `setup populates settings theme list`() {

        initSUT()

        val expected = listOf(
                Selected(BottomSheetItem(DAY.ordinal, DAY.icon, StringHolder(DAY.label)), false),
                Selected(BottomSheetItem(AUTO.ordinal, AUTO.icon, StringHolder(AUTO.label)), true),
                Selected(BottomSheetItem(NIGHT.ordinal, NIGHT.icon, StringHolder(NIGHT.label)), false)
        )

        sut.outputs.themePreferences.test {
            assertValue(expected)
        }
    }

    @Test
    fun `setup populates settings animation list`() {

        initSUT()

        val expected = listOf(
            Selected(BottomSheetItem(NONE.ordinal, NONE.icon, StringHolder(NONE.label)), false),
            Selected(BottomSheetItem(QUICK.ordinal, QUICK.icon, StringHolder(QUICK.label)), false),
            Selected(BottomSheetItem(MEDIUM.ordinal, MEDIUM.icon, StringHolder(MEDIUM.label)), true),
            Selected(BottomSheetItem(SLOW.ordinal, SLOW.icon, StringHolder(SLOW.label)), false)
        )

        sut.outputs.animationPreference.test {
            assertValue(expected)
        }
    }

    @Test
    fun `setup populates default season`() {

        every { seasonController.supportedSeasons } returns setOf(2019, 2020)
        every { seasonController.isUserDefinedValueSet } returns false

        initSUT()

        val expected = mutableListOf<Selected<BottomSheetItem>>().apply {
            add(Selected(BottomSheetItem(id = -1, text = StringHolder(R.string.settings_default_season_option_automatic)), true))
        }

        sut.outputs.defaultSeasonPreference.test {
            assertValue(expected)
        }
    }

    @Test
    fun `setup populates default season when user default set`() {

        every { seasonController.supportedSeasons } returns setOf(2019, 2020)
        every { seasonController.isUserDefinedValueSet } returns true
        every { seasonController.defaultSeason } returns 2019

        initSUT()

        val expected = mutableListOf<Selected<BottomSheetItem>>().apply {
            add(Selected(BottomSheetItem(id = -1, text = StringHolder(R.string.settings_default_season_option_automatic)), false))
            add(Selected(BottomSheetItem(id = 0, text = StringHolder(R.string.settings_default_season_option_user, 2019)), true))
        }

        sut.outputs.defaultSeasonPreference.test {
            assertValue(expected)
        }
    }

    @Test
    fun `select a theme updates the pref`() {

        initSUT()

        sut.pickTheme(NIGHT)

        verify { appearanceController.currentTheme = NIGHT }

        sut.outputs.themeChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `select a bar animation updates the preference`() {

        initSUT()

        sut.pickAnimationSpeed(SLOW)
        verify { appearanceController.animationSpeed = SLOW }

        sut.outputs.animationChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `select a default season updates the preference to auto`() {

        initSUT()

        sut.pickSeason(null)
        verify { seasonController.clearDefault() }

        sut.outputs.defaultSeasonChanged.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting theme pref opens picker`() {

        initSUT()

        sut.inputs.preferenceClicked(THEME, null)

        sut.outputs.openThemePicker.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting animation pref opens picker`() {

        initSUT()

        sut.inputs.preferenceClicked(ANIMATION_SPEED, null)

        sut.outputs.openAnimationPicker.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting default season pref opens picker`() {

        initSUT()

        sut.inputs.preferenceClicked(DEFAULT_SEASON, null)

        sut.outputs.openDefaultSeasonPicker.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting qualifying delta pref updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(QUALIFYING_DELTAS, true)

        verify { raceController.showQualifyingDelta = true }
    }

    @Test
    fun `selecting fade dnf updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(FADE_OUT_DNF, true)

        verify { raceController.fadeDNF = true }
    }

    @Test
    fun `selecting qualifying grid penalty updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(QUALIFYING_GRID_PENALTY, true)

        verify { raceController.showGridPenaltiesInQualifying = true }
    }

    @Test
    fun `selecting season bottom sheet favourited updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_FAVOURITED, true)

        verify { seasonController.favouritesExpanded = true }
    }

    @Test
    fun `selecting season bottom sheet all updates value properly`() {

        initSUT()

        sut.inputs.preferenceClicked(SEASON_BOTTOM_SHEET_ALL, true)

        verify { seasonController.allExpanded = true }
    }

    @Test
    fun `selecting about fires open about event`() {

        initSUT()

        sut.inputs.preferenceClicked(ABOUT, null)

        sut.outputs.openAbout.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting refresh widgets fires refresh widgets event`() {

        initSUT()

        sut.inputs.preferenceClicked(WIDGETS_REFRESH_ALL, null)

        sut.outputs.refreshWidgets.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting review fires open review event with package id`() {

        initSUT()

        sut.inputs.preferenceClicked(REVIEW, null)

        sut.outputs.openReview.test {
            assertDataEventValue("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)
        }
    }

    @Test
    fun `selecting release fires open release notes event`() {

        initSUT()

        sut.inputs.preferenceClicked(RELEASE, null)

        sut.outputs.openRelease.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting crash reporting updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(CRASH, true)

        verify { crashController.enabled = true }
    }

    @Test
    fun `selecting analytics updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(ANALYTICS, true)

        verify { analyticsController.enabled = true }
    }

    @Test
    fun `selecting suggestions fires open suggestions event`() {

        initSUT()

        sut.inputs.preferenceClicked(SUGGESTION, null)

        sut.outputs.openSuggestions.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting shake to report updates value`() {

        initSUT()

        sut.inputs.preferenceClicked(SHAKE, true)

        verify { deviceController.shakeToReport = true }
    }

    @Test
    fun `selecting news fires open news settings event`() {

        initSUT()

        sut.inputs.preferenceClicked(NEWS, null)

        sut.outputs.openNews.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting notification qualifying channel fires notification event`() {

        initSUT()

        sut.inputs.preferenceClicked(NOTIFICATIONS_CHANNEL_QUALIFYING, null)

        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue(topicQualifying)
        }
    }

    @Test
    fun `selecting notification race channel fires notification event`() {

        initSUT()

        sut.inputs.preferenceClicked(NOTIFICATIONS_CHANNEL_RACE, null)

        sut.outputs.openNotificationsChannel.test {
            assertDataEventValue(topicRace)
        }
    }

    @Test
    fun `selecting notification settings fires notification event`() {

        initSUT()

        sut.inputs.preferenceClicked(NOTIFICATIONS_SETTINGS, null)

        sut.outputs.openNotifications.test {
            assertEventFired()
        }
    }
}