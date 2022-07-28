package tmg.flashback.ui.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import tmg.flashback.ads.config.Adverts
import tmg.flashback.ads.config.ui.settings.adverts.SettingsAdvertScreenVM
import tmg.flashback.rss.RSS
import tmg.flashback.rss.RSSConfigure
import tmg.flashback.rss.ui.configure.SettingsRSSConfigureScreenVM
import tmg.flashback.rss.ui.settings.SettingsRSSScreenVM
import tmg.flashback.settings.About
import tmg.flashback.settings.Appearance
import tmg.flashback.settings.PrivacyPolicy
import tmg.flashback.settings.Support
import tmg.flashback.settings.ui.privacypolicy.PrivacyPolicyScreenVM
import tmg.flashback.settings.ui.settings.about.SettingsAboutScreenVM
import tmg.flashback.settings.ui.settings.appearance.SettingsAppearanceScreenVM
import tmg.flashback.stats.Home
import tmg.flashback.stats.Notifications
import tmg.flashback.stats.ui.settings.home.SettingsHomeScreenVM
import tmg.flashback.stats.ui.settings.notifications.SettingsNotificationScreenVM
import tmg.flashback.ui.All
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.web.WebBrowser
import tmg.flashback.web.ui.settings.SettingsWebBrowserScreenVM

fun NavGraphBuilder.appSettings(navController: NavController) {
    composable(Screen.Settings.All.route) {
        SettingsAllScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.Appearance.route) {
        SettingsAppearanceScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.Home.route) {
        SettingsHomeScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.RSS.route) {
        SettingsRSSScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.RSSConfigure.route) {
        SettingsRSSConfigureScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.Notifications.route) {
        SettingsNotificationScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.WebBrowser.route) {
        SettingsWebBrowserScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.Support.route) {
        SettingsWebBrowserScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.Adverts.route) {
        SettingsAdvertScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
    composable(Screen.Settings.About.route) {
        SettingsAboutScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }

    composable(Screen.Settings.PrivacyPolicy.route) {
        PrivacyPolicyScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
}