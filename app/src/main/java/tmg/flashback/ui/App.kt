package tmg.flashback.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.*
import androidx.navigation.compose.*
import org.koin.androidx.compose.inject
import tmg.flashback.releasenotes.ReleaseNotes
import tmg.flashback.releasenotes.releaseNotes
import tmg.flashback.releasenotes.ui.releasenotes.ReleaseScreenVM
import tmg.flashback.rss.rss
import tmg.flashback.stats.Search
import tmg.flashback.stats.WeekendPlaceholder
import tmg.flashback.stats.stats
import tmg.flashback.stats.ui.search.SearchScreenVM
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.dashboard.DashboardScreen
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.navigation.navigate
import tmg.flashback.ui.settings.appSettings

@Composable
fun HomeScreen(
    windowSize: WindowSize
) {
    val navController = rememberNavController()
    val navigator: Navigator by inject()
    val destination by navigator.destination.collectAsState()
    LaunchedEffect(destination) {
        if (navController.currentDestination?.route != destination.route) {
            navController.navigate(destination)
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            DashboardScreen(windowSize = windowSize)
        }

        releaseNotes(navController)

        stats(navController)

        rss(navController)

        appSettings(navController)
    }
}