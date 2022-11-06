package tmg.flashback.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import org.threeten.bp.LocalDate
import tmg.flashback.releasenotes.ReleaseNotes
import tmg.flashback.releasenotes.ui.releasenotes.ReleaseScreenVM
import tmg.flashback.rss.RSS
import tmg.flashback.rss.RSSConfigure
import tmg.flashback.rss.ui.configure.ConfigureRSSScreenVM
import tmg.flashback.rss.ui.feed.RSSScreenVM
import tmg.flashback.settings.PrivacyPolicy
import tmg.flashback.settings.ui.privacypolicy.PrivacyPolicyScreenVM
import tmg.flashback.stats.Circuit
import tmg.flashback.stats.Constructor
import tmg.flashback.stats.Driver
import tmg.flashback.stats.DriverSeason
import tmg.flashback.stats.Search
import tmg.flashback.stats.Weekend
import tmg.flashback.stats.ui.circuits.CircuitScreenVM
import tmg.flashback.stats.ui.constructors.overview.ConstructorOverviewScreenVM
import tmg.flashback.stats.ui.drivers.overview.DriverOverviewScreenVM
import tmg.flashback.stats.ui.drivers.season.DriverSeasonScreenVM
import tmg.flashback.stats.ui.search.SearchScreenVM
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.WeekendScreenVM
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.dashboard.compact.DashboardScreenVM
import tmg.flashback.ui.dashboard.expanded.DashboardExpandedNavItem
import tmg.flashback.ui.dashboard.expanded.DashboardExpandedScreenState
import tmg.flashback.ui.dashboard.expanded.DashboardExpandedScreenVM
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.navigation.asNavigationDestination
import tmg.flashback.ui.navigation.navIntRequired
import tmg.flashback.ui.navigation.navStringRequired
import tmg.flashback.ui.navigation.navigate
import tmg.flashback.ui.settings.About
import tmg.flashback.ui.settings.Ads
import tmg.flashback.ui.settings.All
import tmg.flashback.ui.settings.Home
import tmg.flashback.ui.settings.NotificationsResults
import tmg.flashback.ui.settings.NotificationsUpcoming
import tmg.flashback.ui.settings.Privacy
import tmg.flashback.ui.settings.SettingsAllScreenVM
import tmg.flashback.ui.settings.Web
import tmg.flashback.ui.settings.about.SettingsAboutScreenVM
import tmg.flashback.ui.settings.about.SettingsPrivacyScreenVM
import tmg.flashback.ui.settings.ads.SettingsAdsScreenVM
import tmg.flashback.ui.settings.layout.SettingsLayoutScreen
import tmg.flashback.ui.settings.layout.SettingsLayoutScreenVM
import tmg.flashback.ui.settings.notifications.SettingsNotificationsResultsScreenVM
import tmg.flashback.ui.settings.notifications.SettingsNotificationsUpcomingScreenVM
import tmg.flashback.ui.settings.web.SettingsWebScreenVM
import tmg.utilities.extensions.toLocalDate

@Composable
fun HomeScreen(
    windowSize: WindowSize,
    navigator: Navigator,
    closeApp: () -> Unit,
) {
    val navController = rememberNavController()
    val destination by navigator.destination.collectAsState()

    LaunchedEffect(destination) {
        if (navController.currentDestination?.route != destination.route) {
            if (destination == Screen.Home) {
                navController.navigate(destination) {
                    this.launchSingleTop = true
                }
            } else {
                navController.navigate(destination)
            }
        }
    }
    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            destination.route?.asNavigationDestination()?.let {
                navigator.navigate(it)
            }
        }
    }

    BackHandler {
        if (!navController.popBackStack()) {
            closeApp()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        composable(Screen.Home.route) {
            DashboardScreenVM()
        }

        // Release Notes
        composable(Screen.ReleaseNotes.route) {
            ReleaseScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }

        // Privacy Policy
        composable(Screen.Settings.PrivacyPolicy.route) {
            PrivacyPolicyScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }

        // Settings
        composable(Screen.Settings.All.route) {
            SettingsAllScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.Home.route) {
            SettingsLayoutScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.Web.route) {
            SettingsWebScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.NotificationsUpcoming.route) {
            SettingsNotificationsUpcomingScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.NotificationsResults.route) {
            SettingsNotificationsResultsScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.Ads.route) {
            SettingsAdsScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.Privacy.route) {
            SettingsPrivacyScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.About.route) {
            SettingsAboutScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }

        // Stats
        composable(Screen.Weekend.route, arguments = listOf(
            navIntRequired("season"),
            navIntRequired("round")
        )) {
            val season = it.arguments?.getInt("season")!!
            val round = it.arguments?.getInt("round")!!
            WeekendScreenVM(
                weekendInfo = WeekendInfo(
                    season = season,
                    round = round,
                    raceName = it.arguments?.getString("raceName") ?: "",
                    circuitId = it.arguments?.getString("circuitId") ?: "",
                    circuitName = it.arguments?.getString("circuitName") ?: "",
                    country = it.arguments?.getString("country") ?: "",
                    countryISO = it.arguments?.getString("countryISO") ?: "",
                    date = it.arguments?.getString("date")?.toLocalDate("yyyy-MM-dd") ?: LocalDate.now(),
                ),
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Circuit.route, arguments = listOf(
            navStringRequired("circuitId")
        )) {
            val circuitId = it.arguments?.getString("circuitId")!!
            CircuitScreenVM(
                circuitId = circuitId,
                circuitName = it.arguments?.getString("circuitName") ?: "",
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Driver.route, arguments = listOf(
            navStringRequired("driverId")
        )) {
            val driverId = it.arguments?.getString("driverId")!!
            DriverOverviewScreenVM(
                driverId = driverId,
                driverName = it.arguments?.getString("driverName") ?: "",
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.DriverSeason.route, arguments = listOf(
            navStringRequired("driverId"),
            navIntRequired("season")
        )) {
            val driverId = it.arguments?.getString("driverId")!!
            val season = it.arguments?.getInt("season")!!
            DriverSeasonScreenVM(
                driverId = driverId,
                driverName = it.arguments?.getString("driverName") ?: "",
                season = season,
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Constructor.route, arguments = listOf(
            navStringRequired("constructorId")
        )) {
            val constructorId = it.arguments?.getString("constructorId")!!
            ConstructorOverviewScreenVM(
                constructorId = constructorId,
                constructorName = it.arguments?.getString("constructorName") ?: "",
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(
            Screen.Search.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://search" })
        ) {
            SearchScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }

        // RSS
        composable(
            Screen.RSS.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://rss" })
        ) {
            RSSScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.RSSConfigure.route) {
            ConfigureRSSScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
    }
}