package tmg.flashback.ui

import android.os.Build
import android.os.Parcelable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.window.layout.WindowLayoutInfo
import tmg.flashback.ads.ads.components.AdvertProvider
import tmg.flashback.circuits.contract.Circuit
import tmg.flashback.circuits.contract.model.ScreenCircuitData
import tmg.flashback.circuits.ui.CircuitScreenVM
import tmg.flashback.constructors.contract.Constructor
import tmg.flashback.constructors.contract.model.ScreenConstructorData
import tmg.flashback.constructors.presentation.overview.ConstructorOverviewScreenVM
import tmg.flashback.drivers.contract.Driver
import tmg.flashback.drivers.contract.model.ScreenDriverData
import tmg.flashback.drivers.presentation.overview.DriverOverviewScreenVM
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.navigation.asNavigationDestination
import tmg.flashback.privacypolicy.contract.PrivacyPolicy
import tmg.flashback.privacypolicy.ui.PrivacyPolicyScreenVM
import tmg.flashback.rss.contract.RSS
import tmg.flashback.rss.contract.RSSConfigure
import tmg.flashback.rss.ui.configure.ConfigureRSSScreenVM
import tmg.flashback.rss.ui.feed.RSSScreenVM
import tmg.flashback.search.contract.Search
import tmg.flashback.search.presentation.SearchScreenVM
import tmg.flashback.season.contract.ConstructorsStandings
import tmg.flashback.season.contract.DriverStandings
import tmg.flashback.season.contract.Races
import tmg.flashback.season.presentation.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.season.presentation.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.season.presentation.dashboard.races.RacesScreenVM
import tmg.flashback.ui.settings.About
import tmg.flashback.ui.settings.Ads
import tmg.flashback.ui.settings.All
import tmg.flashback.ui.settings.Home
import tmg.flashback.ui.settings.NightMode
import tmg.flashback.ui.settings.NotificationsUpcomingNotice
import tmg.flashback.ui.settings.Privacy
import tmg.flashback.ui.settings.SettingsAllScreenVM
import tmg.flashback.ui.settings.Theme
import tmg.flashback.ui.settings.Weather
import tmg.flashback.ui.settings.Web
import tmg.flashback.ui.settings.about.SettingsAboutScreenVM
import tmg.flashback.ui.settings.about.SettingsPrivacyScreenVM
import tmg.flashback.ui.settings.ads.SettingsAdsScreenVM
import tmg.flashback.ui.settings.appearance.nightmode.SettingsNightModeScreenVM
import tmg.flashback.ui.settings.appearance.theme.SettingsThemeScreenVM
import tmg.flashback.ui.settings.data.SettingsLayoutScreenVM
import tmg.flashback.ui.settings.data.SettingsWeatherScreenVM
import tmg.flashback.ui.settings.notifications.SettingsNotificationUpcomingNoticeScreenVM
import tmg.flashback.ui.settings.web.SettingsWebScreenVM
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.WeekendScreenVM

@Composable
fun AppGraph(
    openMenu: () -> Unit,
    navController: NavHostController,
    deeplink: String?,
    windowSize: WindowSizeClass,
    windowInfo: WindowLayoutInfo,
    navigator: Navigator,
    closeApp: () -> Unit,
    advertProvider: AdvertProvider,
    modifier: Modifier = Modifier
) {
    val isCompact = windowSize.widthSizeClass == WindowWidthSizeClass.Compact

    NavHost(
        navController = navController,
        startDestination = Screen.Races.route,
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        composable(Screen.Races.route) {
            RacesScreenVM(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize
            )
        }

        composable(Screen.ConstructorsStandings.route) {
            ConstructorStandingsScreenVM(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
            )
        }

        composable(Screen.DriverStandings.route) {
            DriverStandingsScreenVM(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize
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
        composable(Screen.Settings.Theme.route) {
            SettingsThemeScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.NightMode.route) {
            SettingsNightModeScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.Home.route) {
            SettingsLayoutScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.Weather.route) {
            SettingsWeatherScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.Web.route) {
            SettingsWebScreenVM(
                actionUpClicked = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.NotificationsUpcomingNotice.route) {
            SettingsNotificationUpcomingNoticeScreenVM(
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
                actionUpClicked = { navController.popBackStack() },
                windowSizeClass = windowSize,
                weekendInfo = screenWeekendData,
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
                actionUpClicked = { navController.popBackStack() },
                windowSizeClass = windowSize,
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
                actionUpClicked = { navController.popBackStack() },
                windowSizeClass = windowSize,
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
                actionUpClicked = { navController.popBackStack() },
                windowSizeClass = windowSize,
            )
        }

        composable(
            Screen.Search.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://search" })
        ) {
            SearchScreenVM(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize
            )
        }

        // RSS
        composable(
            Screen.RSS.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://rss" })
        ) {
            RSSScreenVM(
                windowSizeClass = windowSize,
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

    DisposableEffect(Unit) {
        deeplink?.let {
            if (it.isNotEmpty()) {
                navigator.navigate(it.asNavigationDestination())
            }
        }
        this.onDispose { }
    }
}

private inline fun <reified T: Parcelable> NavBackStackEntry.getArgument(key: String): T {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.arguments?.getParcelable(key, T::class.java)!!
    } else {
        this.arguments?.getParcelable<T>(key)!!
    }
}