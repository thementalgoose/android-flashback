package tmg.flashback.releasenotes

import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.ReleaseNotes: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "release_notes"
    }

class ReleaseNotesNavigationComponent @Inject constructor(
    private val navigator: Navigator
) {
    fun releaseNotes() {
        navigator.navigate(Screen.ReleaseNotes)
    }
}