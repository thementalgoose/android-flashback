package tmg.flashback.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.inject
import tmg.flashback.settings.About
import tmg.flashback.settings.Appearance
import tmg.flashback.settings.ui.settings.appearance.SettingsAppearanceScreenVM
import tmg.flashback.stats.Home
import tmg.flashback.stats.Weekend
import tmg.flashback.stats.WeekendPlaceholder
import tmg.flashback.stats.ui.weekend.WeekendInfo
import tmg.flashback.stats.ui.weekend.WeekendScreen
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.utils.WindowSize
import tmg.flashback.ui.dashboard.DashboardScreen
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import tmg.flashback.ui.settings.SettingsAllScreenVM
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

    NavHost(
        navController = navController,
        startDestination = navigator.destination.value.route
    ) {
        composable(Screen.Home.route) {
            DashboardScreen(windowSize = windowSize)
        }

        composable(
            Screen.WeekendPlaceholder,
            arguments = listOf(
                navArgument("season") { this.type = NavType.IntType },
                navArgument("round") { this.type = NavType.IntType }
            )
        ) {
            val season = it.arguments?.getInt("season")
            val round = it.arguments?.getInt("round")

            TextBody1(text = "SEASON $season ROUND $round")
        }

        appSettings(navController)
    }
}

fun NavController.navigate(destination: NavigationDestination) {
    this.navigate(destination.route)
}