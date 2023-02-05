package tmg.flashback.releasenotes

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.releasenotes.ui.releasenotes.NewReleaseBottomSheetFragment
import tmg.flashback.ui.navigation.ActivityProvider
import tmg.flashback.ui.navigation.NavigationDestination
import tmg.flashback.ui.navigation.Navigator
import tmg.flashback.ui.navigation.Screen
import javax.inject.Inject

val Screen.ReleaseNotes: NavigationDestination
    get() = object : NavigationDestination {
        override val route: String = "release_notes"
    }

class ReleaseNotesNavigationComponent @Inject constructor(
    private val navigator: Navigator,
    private val activityProvider: ActivityProvider
) {
    fun releaseNotes() {
        navigator.navigate(Screen.ReleaseNotes)
    }

    fun releaseNotesNext() {
        activityProvider.launch {
            val activity = it as? AppCompatActivity ?: return@launch
            NewReleaseBottomSheetFragment().show(activity.supportFragmentManager, "RELEASE_NOTES_UP_NEXT")
        }
    }
}