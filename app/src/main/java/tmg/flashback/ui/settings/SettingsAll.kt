package tmg.flashback.ui.settings

import androidx.compose.runtime.Composable
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.inject
import tmg.flashback.settings.ui.settings.appearance.SettingsAppearanceScreenVM
import tmg.flashback.ui.navigation.NavigationService

@Composable
fun SettingsAll() {
    val navigationService: NavigationService by inject()
    NavHost(
        navController = navigationService.navController,
        startDestination = "all"
    ) {
        composable("all") {
            SettingsAllScreenVM(actionUpClicked = {
                navigationService.navController.navigate("all") {
                    this.launchSingleTop = true
                }
            })
        }
        composable("appearance") {
            SettingsAppearanceScreenVM(actionUpClicked = { navigationService.navController.popBackStack() })
        }
    }
}