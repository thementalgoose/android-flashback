package tmg.flashback.releasenotes

import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import tmg.flashback.releasenotes.ui.releasenotes.ReleaseScreenVM
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.ReleaseNotes: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "release_notes"
    }

fun NavGraphBuilder.releaseNotes(navController: NavController) {
    composable(Screen.ReleaseNotes.route) {
        ReleaseScreenVM(
            actionUpClicked = { navController.popBackStack() }
        )
    }
}

class ReleaseNotesNavigationComponent @Inject constructor(
    private val navigator: Navigator
) {
    fun releaseNotes() {
        navigator.navigate(Screen.ReleaseNotes)
    }
}