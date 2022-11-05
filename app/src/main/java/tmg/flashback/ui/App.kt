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
import tmg.flashback.releasenotes.releaseNotes
import tmg.flashback.rss.rss
import tmg.flashback.settings.misc
import tmg.flashback.stats.stats
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.dashboard.DashboardScreen
import tmg.flashback.ui.dashboard.DashboardScreenVM
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.navigation.asNavigationDestination
import tmg.flashback.ui.navigation.navigate
import tmg.flashback.ui.settings.settings

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

        releaseNotes(navController)

        misc(navController)

        settings(navController)

        stats(navController)

        rss(navController)
    }
}