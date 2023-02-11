package tmg.flashback.ui.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.settings.about.SettingsAboutScreenVM
import tmg.flashback.ui.settings.about.SettingsPrivacyScreenVM
import tmg.flashback.ui.settings.ads.SettingsAdsScreenVM
import tmg.flashback.ui.settings.layout.SettingsLayoutScreenVM
import tmg.flashback.ui.settings.notifications.SettingsNotificationsResultsScreenVM
import tmg.flashback.ui.settings.notifications.SettingsNotificationsUpcomingScreen
import tmg.flashback.ui.settings.notifications.SettingsNotificationsUpcomingScreenVM
import tmg.flashback.ui.settings.web.SettingsWebScreen
import tmg.flashback.ui.settings.web.SettingsWebScreenVM

internal val Screen.Settings.All: NavigationDestination
    get() = NavigationDestination("settings")

internal val Screen.Settings.Adverts: NavigationDestination
    get() = NavigationDestination("settings/adverts")

internal val Screen.Settings.Home: NavigationDestination
    get() = NavigationDestination("settings/home")


internal val Screen.Settings.Web: NavigationDestination
    get() = NavigationDestination("settings/web")


internal val Screen.Settings.NotificationsUpcoming: NavigationDestination
    get() = NavigationDestination("settings/notifications/upcoming")

internal val Screen.Settings.NotificationsResults: NavigationDestination
    get() = NavigationDestination("settings/notifications/results")


internal val Screen.Settings.Ads: NavigationDestination
    get() = NavigationDestination("settings/ads")

internal val Screen.Settings.Privacy: NavigationDestination
    get() = NavigationDestination("settings/privacy")

internal val Screen.Settings.About: NavigationDestination
    get() = NavigationDestination("settings/about")