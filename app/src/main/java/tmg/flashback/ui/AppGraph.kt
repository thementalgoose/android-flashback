package tmg.flashback.ui

import android.os.Build
import android.os.Parcelable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import tmg.flashback.circuits.presentation.CircuitScreenVM
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
import tmg.flashback.privacypolicy.presentation.PrivacyPolicyScreenVM
import tmg.flashback.rss.contract.RSS
import tmg.flashback.rss.ui.configure.ConfigureRSSScreenVM
import tmg.flashback.rss.ui.feed.RSSScreenVM
import tmg.flashback.search.contract.Search
import tmg.flashback.search.presentation.SearchScreenVM
import tmg.flashback.season.contract.ConstructorsStandings
import tmg.flashback.season.contract.DriverStandings
import tmg.flashback.season.contract.Races
import tmg.flashback.season.presentation.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.season.presentation.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.season.presentation.dashboard.races.RacesScreen
import tmg.flashback.ui.settings.All
import tmg.flashback.ui.settings.SettingsAllScreenVM
import tmg.flashback.ui.settings.about.SettingsPrivacyScreenVM
import tmg.flashback.weekend.contract.Weekend
import tmg.flashback.weekend.contract.model.ScreenWeekendData
import tmg.flashback.weekend.ui.WeekendScreen

@Composable
fun AppGraph(
    openMenu: () -> Unit,
    navController: NavHostController,
    deeplink: String?,
    isRoot: (String, Boolean) -> Unit,
    windowSize: WindowSizeClass,
    windowInfo: WindowLayoutInfo,
    navigator: Navigator,
    closeApp: () -> Unit,
    advertProvider: AdvertProvider,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Races.route,
        modifier = modifier
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        composable(Screen.Races.route) {
            RacesScreen(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.Races.route, !it) },
                advertProvider = advertProvider
            )
        }

        composable(Screen.ConstructorsStandings.route) {
            ConstructorStandingsScreenVM(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.ConstructorsStandings.route, !it) }
            )
        }

        composable(Screen.DriverStandings.route) {
            DriverStandingsScreenVM(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.DriverStandings.route, !it) }
            )
        }

        composable(Screen.Settings.All.route) {
            SettingsAllScreenVM(
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.Settings.All.route, !it) },
            )
        }

        composable(Screen.Settings.PrivacyPolicy.route) {
            PrivacyPolicyScreenVM(
                windowSizeClass = windowSize,
                actionUpClicked = { navController.popBackStack() }
            )
        }

        // TODO: Remove?
        composable(
            Screen.Weekend.route, arguments = listOf(
                navArgument("data") { type = ScreenWeekendData.NavType }
        )) {
            val screenWeekendData = it.getArgument<ScreenWeekendData>("data")
            WeekendScreen(
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
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.Search.route, !it) },
                advertProvider = advertProvider
            )
        }

        // RSS
        composable(
            Screen.RSS.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://rss" })
        ) {
            RSSScreenVM(
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.RSS.route, !it) },
                advertProvider = advertProvider,
                actionUpClicked = openMenu
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