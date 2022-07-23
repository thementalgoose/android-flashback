package tmg.flashback.releasenotes

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen

val Screen.ReleaseNotes: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "release_notes"
    }

class ReleaseNotesNavigationComponent(
    private val navigator: Navigator
) {
    fun releaseNotes() {
        navigator.navigate(Screen.ReleaseNotes)
    }
}