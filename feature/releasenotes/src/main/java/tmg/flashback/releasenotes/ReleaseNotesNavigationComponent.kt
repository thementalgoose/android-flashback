package tmg.flashback.releasenotes

import androidx.appcompat.app.AppCompatActivity
import tmg.flashback.releasenotes.ui.releasenotes.NewReleaseBottomSheetFragment
import tmg.flashback.navigation.ActivityProvider
import tmg.flashback.navigation.NavigationDestination
import tmg.flashback.navigation.Screen
import javax.inject.Inject

val Screen.ReleaseNotes: NavigationDestination
    get() = NavigationDestination("release_notes")

class ReleaseNotesNavigationComponent @Inject constructor(
    private val activityProvider: ActivityProvider
) {
    fun releaseNotesNext() {
        activityProvider.launch {
            val activity = it as? AppCompatActivity ?: return@launch
            NewReleaseBottomSheetFragment().show(activity.supportFragmentManager, "RELEASE_NOTES_UP_NEXT")
        }
    }
}