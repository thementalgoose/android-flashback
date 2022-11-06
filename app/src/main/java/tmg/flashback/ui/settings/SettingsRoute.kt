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
    get() = object : NavigationDestination {
        override val route: String = "settings"
    }

internal val Screen.Settings.Appearance: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/adverts"
    }

internal val Screen.Settings.Home: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/home"
    }


internal val Screen.Settings.Web: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/web"
    }


internal val Screen.Settings.NotificationsUpcoming: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/notifications/upcoming"
    }

internal val Screen.Settings.NotificationsResults: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/notifications/results"
    }


internal val Screen.Settings.Ads: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/ads"
    }

internal val Screen.Settings.Privacy: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/privacy"
    }

internal val Screen.Settings.About: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "settings/about"
    }