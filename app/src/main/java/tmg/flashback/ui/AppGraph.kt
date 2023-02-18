package tmg.flashback.ui

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.window.layout.WindowLayoutInfo
import org.threeten.bp.LocalDate
import tmg.flashback.ads.contract.components.AdvertProvider
import tmg.flashback.privacypolicy.PrivacyPolicy
import tmg.flashback.privacypolicy.ui.PrivacyPolicyScreenVM
import tmg.flashback.releasenotes.ReleaseNotes
import tmg.flashback.releasenotes.ui.releasenotes.ReleaseScreenVM
import tmg.flashback.rss.RSS
import tmg.flashback.rss.RSSConfigure
import tmg.flashback.rss.ui.configure.ConfigureRSSScreenVM
import tmg.flashback.rss.ui.feed.RSSScreenVM
import tmg.flashback.stats.Calendar
import tmg.flashback.stats.Circuit
import tmg.flashback.stats.Constructor
import tmg.flashback.stats.ConstructorSeason
import tmg.flashback.stats.Constructors
import tmg.flashback.stats.Driver
import tmg.flashback.stats.DriverSeason
import tmg.flashback.stats.Drivers
import tmg.flashback.stats.Search
import tmg.flashback.stats.Weekend
import tmg.flashback.stats.ui.circuits.CircuitScreenVM
import tmg.flashback.stats.ui.constructors.overview.ConstructorOverviewScreenVM
import tmg.flashback.stats.ui.constructors.season.ConstructorSeasonScreenVM
import tmg.flashback.stats.ui.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.stats.ui.dashboard.schedule.ScheduleScreenVM
import tmg.flashback.stats.ui.drivers.overview.DriverOverviewScreenVM
import tmg.flashback.stats.ui.drivers.season.DriverSeasonScreenVM
import tmg.flashback.stats.ui.search.SearchScreenVM
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.WeekendScreenVM
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.navigation.asNavigationDestination
import tmg.flashback.ui.navigation.navIntRequired
import tmg.flashback.ui.navigation.navString
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
import tmg.flashback.ui.settings.layout.SettingsLayoutScreenVM
import tmg.flashback.ui.settings.notifications.SettingsNotificationsResultsScreenVM
import tmg.flashback.ui.settings.notifications.SettingsNotificationsUpcomingScreenVM
import tmg.flashback.ui.settings.web.SettingsWebScreenVM
import tmg.utilities.extensions.toLocalDate

@Composable
fun AppGraph(
    openMenu: () -> Unit,
    windowSize: WindowSizeClass,
    windowInfo: WindowLayoutInfo,
    navigator: Navigator,
    closeApp: () -> Unit,
    advertProvider: AdvertProvider,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val destination by navigator.destination.collectAsState()

    val isCompact = windowSize.widthSizeClass == WindowWidthSizeClass.Compact

    LaunchedEffect(destination) {
        if (navController.currentDestination?.route != destination?.route) {
            val dest = destination ?: return@LaunchedEffect
            navController.navigate(dest)
        }
    }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            destination.route?.asNavigationDestination()?.let {
                navigator.navigate(it)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        composable(Screen.Calendar.route, arguments = listOf(
            navString("season")
        )) {
            // Has to be nullable because initial navigation graph
            //  value cannot contain placeholder values
            val season = it.arguments?.getString("season")?.toInt() ?: 2023
            ScheduleScreenVM(
                menuClicked = openMenu,
                showMenu = isCompact,
                season = season
            )
        }

        composable(Screen.Constructors.route, arguments = listOf(
            navIntRequired("season")
        )) {
            val season = it.arguments?.getInt("season")!!
            ConstructorStandingsScreenVM(
                menuClicked = openMenu,
                showMenu = isCompact,
                season = season
            )
        }

        composable(Screen.Drivers.route, arguments = listOf(
            navIntRequired("season")
        )) {
            val season = it.arguments?.getInt("season")!!
            DriverStandingsScreenVM(
                menuClicked = openMenu,
                showMenu = isCompact,
                season = season
            )
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
                showMenu = windowSize.widthSizeClass == WindowWidthSizeClass.Compact,
                actionUpClicked = openMenu
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
            val weekendInfo = WeekendInfo(
                season = season,
                round = round,
                raceName = it.arguments?.getString("raceName") ?: "",
                circuitId = it.arguments?.getString("circuitId") ?: "",
                circuitName = it.arguments?.getString("circuitName") ?: "",
                country = it.arguments?.getString("country") ?: "",
                countryISO = it.arguments?.getString("countryISO") ?: "",
                date = it.arguments?.getString("date")?.toLocalDate("yyyy-MM-dd") ?: LocalDate.now(),
            )
            WeekendScreenVM(
                weekendInfo = weekendInfo,
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

        composable(Screen.ConstructorSeason.route, arguments = listOf(
            navStringRequired("constructorId"),
            navIntRequired("season")
        )) {
            val constructorId = it.arguments?.getString("constructorId")!!
            val season = it.arguments?.getInt("season")!!
            ConstructorSeasonScreenVM(
                constructorId = constructorId,
                constructorName = it.arguments?.getString("constructorName") ?: "",
                season = season,
                actionUpClicked = { navController.popBackStack() }
            )
        }

        composable(
            Screen.Search.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://search" })
        ) {
            SearchScreenVM(
                advertProvider = advertProvider,
                showMenu = windowSize.widthSizeClass == WindowWidthSizeClass.Compact,
                actionUpClicked = openMenu
            )
        }

        // RSS
        composable(
            Screen.RSS.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://rss" })
        ) {
            RSSScreenVM(
                advertProvider = advertProvider,
                showMenu = windowSize.widthSizeClass == WindowWidthSizeClass.Compact,
                actionUpClicked = openMenu
            )
        }
        composable(Screen.Settings.RSSConfigure.route) {
            ConfigureRSSScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
    }
}