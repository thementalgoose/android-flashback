package tmg.flashback.presentation

import android.os.Build
import android.os.Parcelable
import androidx.compose.foundation.layout.PaddingValues
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
import tmg.flashback.circuits.navigation.ScreenCircuitData
import tmg.flashback.circuits.presentation.CircuitScreenVM
import tmg.flashback.constructors.navigation.ScreenConstructorData
import tmg.flashback.constructors.presentation.overview.ConstructorOverviewScreenVM
import tmg.flashback.drivers.navigation.ScreenDriverData
import tmg.flashback.drivers.presentation.overview.DriverOverviewScreenVM
import tmg.flashback.navigation.Navigator
import tmg.flashback.navigation.Screen
import tmg.flashback.navigation.route
import tmg.flashback.presentation.settings.SettingsAllScreenVM
import tmg.flashback.privacypolicy.presentation.PrivacyPolicyScreenVM
import tmg.flashback.reactiongame.presentation.ReactionScreenVM
import tmg.flashback.rss.presentation.feed.RSSScreenVM
import tmg.flashback.search.presentation.SearchScreenVM
import tmg.flashback.season.presentation.dashboard.constructors.ConstructorStandingsScreenVM
import tmg.flashback.season.presentation.dashboard.drivers.DriverStandingsScreenVM
import tmg.flashback.season.presentation.dashboard.races.RacesScreen

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
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Races.route,
        modifier = modifier
    ) {
        composable(Screen.Races.route) {
            RacesScreen(
                paddingValues = paddingValues,
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                deeplink = deeplink,
                isRoot = { isRoot(Screen.Races.route, !it) },
                advertProvider = advertProvider
            )
        }

        composable(Screen.ConstructorStandings.route) {
            ConstructorStandingsScreenVM(
                paddingValues = paddingValues,
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.ConstructorStandings.route, !it) }
            )
        }

        composable(Screen.DriverStandings.route) {
            DriverStandingsScreenVM(
                paddingValues = paddingValues,
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.DriverStandings.route, !it) }
            )
        }

        composable(Screen.Settings.route) {
            SettingsAllScreenVM(
                paddingValues = paddingValues,
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.Settings.route, !it) },
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreenVM(
                paddingValues = paddingValues,
                windowSizeClass = windowSize,
                actionUpClicked = { navController.popBackStack() }
            )
        }

        composable(
            Screen.Circuit.route, arguments = listOf(
                navArgument("data") { type = ScreenCircuitData.NavType }
        )) {
            val screenCircuitData = it.getArgument<ScreenCircuitData>("data")
            CircuitScreenVM(
                paddingValues = paddingValues,
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
                paddingValues = paddingValues,
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
                paddingValues = paddingValues,
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
                paddingValues = paddingValues,
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.Search.route, !it) },
                advertProvider = advertProvider
            )
        }

        // RSS
        composable(
            Screen.Rss.route,
            deepLinks = listOf(navDeepLink { uriPattern = "flashback://rss" })
        ) {
            RSSScreenVM(
                paddingValues = paddingValues,
                windowSizeClass = windowSize,
                isRoot = { isRoot(Screen.Rss.route, !it) },
                advertProvider = advertProvider,
                actionUpClicked = openMenu
            )
        }

        composable(
            Screen.ReactionGame.route,
        ) {
            ReactionScreenVM(
                paddingValues = paddingValues,
                actionUpClicked = openMenu,
                windowSizeClass = windowSize,
            )
        }
    }

    DisposableEffect(Unit) {
        when (deeplink) {
            Screen.Rss.route -> navigator.navigate(Screen.Rss)
            Screen.Search.route -> navigator.navigate(Screen.Search)
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