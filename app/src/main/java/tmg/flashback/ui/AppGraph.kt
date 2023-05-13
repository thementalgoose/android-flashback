package tmg.flashback.ui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.window.layout.WindowLayoutInfo
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.model.ScreenCircuitData
import tmg.flashback.circuits.ui.CircuitScreenVM
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.ConstructorSeason
import tmg.flashback.constructors.contract.model.ScreenConstructorData
import tmg.flashback.constructors.contract.model.ScreenConstructorSeasonData
import tmg.flashback.constructors.ui.overview.ConstructorOverviewScreenVM
import tmg.flashback.constructors.ui.season.ConstructorSeasonScreenVM
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.DriverSeason
import tmg.flashback.drivers.contract.model.ScreenDriverData
import tmg.flashback.drivers.contract.model.ScreenDriverSeasonData
import tmg.flashback.drivers.ui.overview.DriverOverviewScreenVM
import tmg.flashback.drivers.ui.season.DriverSeasonScreenVM
import tmg.flashback.navigation.Screen
import tmg.flashback.navigation.asNavigationDestination
import tmg.flashback.navigation.navIntRequired
import tmg.flashback.navigation.navString
import tmg.flashback.navigation.navStringRequired
import tmg.flashback.navigation.navigate
import tmg.flashback.privacypolicy.contract.PrivacyPolicy
import tmg.flashback.privacypolicy.ui.PrivacyPolicyScreenVM
import tmg.flashback.releasenotes.ReleaseNotes
import tmg.flashback.releasenotes.ui.releasenotes.ReleaseScreenVM
import tmg.flashback.results.Calendar
import tmg.flashback.results.Constructors
import tmg.flashback.results.Drivers
import tmg.flashback.results.ui.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.results.ui.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.results.ui.dashboard.schedule.ScheduleScreenVM
import tmg.flashback.rss.contract.RSS
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.rss.ui.configure.ConfigureRSSScreenVM
import tmg.flashback.rss.ui.feed.RSSScreenVM
import tmg.flashback.search.contract.Search
import tmg.flashback.search.ui.SearchScreenVM
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
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.WeekendScreenVM

@Composable
fun AppGraph(
    openMenu: () -> Unit,
    windowSize: WindowSizeClass,
    windowInfo: WindowLayoutInfo,
    navigator: tmg.flashback.navigation.Navigator,
    closeApp: () -> Unit,
    advertProvider: AdvertProvider,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val destination by navigator.destination.collectAsState()

    val isCompact = windowSize.widthSizeClass == WindowWidthSizeClass.Compact

    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route,
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        composable(
            Screen.Calendar.route, arguments = listOf(
                navString("season")
        )) {
            // Has to be nullable because initial navigation graph
            //  value cannot contain placeholder values
            val season = it.arguments?.getString("season")?.toIntOrNull() ?: 2023
            ScheduleScreenVM(
                menuClicked = openMenu,
                showMenu = isCompact,
                season = season
            )
        }

        composable(
            Screen.Constructors.route, arguments = listOf(
                navStringRequired("season")
        )) {
            val season = it.arguments?.getString("season")!!.toInt()
            ConstructorStandingsScreenVM(
                menuClicked = openMenu,
                showMenu = isCompact,
                season = season
            )
        }

        composable(
            Screen.Drivers.route, arguments = listOf(
                navStringRequired("season")
        )) {
            val season = it.arguments?.getString("season")!!.toInt()
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
        composable(
            Screen.Weekend.route, arguments = listOf(
                navArgument("data") { type = ScreenWeekendData.NavType }
        )) {
            val screenWeekendData = it.getArgument<ScreenWeekendData>("data")
            WeekendScreenVM(
                weekendInfo = screenWeekendData,
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(
            Screen.Circuit.route, arguments = listOf(
                navArgument("data") { type = ScreenCircuitData.NavType }
        )) {
            val screenCircuitData = it.getArgument<ScreenCircuitData>("data")
            CircuitScreenVM(
                circuitId = screenCircuitData.circuitId,
                circuitName = screenCircuitData.circuitName,
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(
            Screen.Driver.route, arguments = listOf(
                navArgument("data") { type = ScreenDriverData.NavType }
        )) {
            val driverData = it.getArgument<ScreenDriverData>("data")
            DriverOverviewScreenVM(
                driverId = driverData.driverId,
                driverName = driverData.driverName,
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(
            Screen.DriverSeason.route, arguments = listOf(
                navArgument("data") { type = ScreenDriverSeasonData.NavType }
        )) {
            val driverData = it.getArgument<ScreenDriverSeasonData>("data")
            DriverSeasonScreenVM(
                driverId = driverData.driverId,
                driverName = driverData.driverName,
                season = driverData.season,
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(
            Screen.Constructor.route, arguments = listOf(
                navArgument("data") { type = ScreenConstructorData.NavType }
        )) {
            val constructorData = it.getArgument<ScreenConstructorData>("data")
            ConstructorOverviewScreenVM(
                constructorId = constructorData.constructorId,
                constructorName = constructorData.constructorName,
                actionUpClicked = { navController.popBackStack() }
            )
        }

        composable(
            Screen.ConstructorSeason.route, arguments = listOf(
                navArgument("data") { type = ScreenConstructorSeasonData.NavType }
        )) {
            val constructorData = it.getArgument<ScreenConstructorSeasonData>("data")
            ConstructorSeasonScreenVM(
                constructorId = constructorData.constructorId,
                constructorName = constructorData.constructorName,
                season = constructorData.season,
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

    // Updates current destination if graph is updated
    LaunchedEffect(destination) {
        if (navController.currentDestination?.route != destination?.route) {
            val dest = destination ?: return@LaunchedEffect
            navController.navigate(dest)
        }
    }

    // Updates navigation destination in navigator if
    //  nav controller updates outside of Navigator
    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            destination.route?.asNavigationDestination()?.let {
                navigator.navigate(it)
            }
        }
    }
}

private inline fun <reified T: Parcelable> NavBackStackEntry.getArgument(key: String): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.arguments?.getParcelable(key, T::class.java)!!
    } else {
        this.arguments?.getParcelable<T>(key)!!
    }
}