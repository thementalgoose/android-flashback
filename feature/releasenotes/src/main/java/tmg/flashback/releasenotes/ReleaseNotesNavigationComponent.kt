package tmg.flashback.releasenotes

import tmg.flashback.releasenotes.ui.releasenotes.ReleaseActivity
import tmg.flashback.ui.navigation.ActivityProvider

class ReleaseNotesNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    private fun releaseNotesIntent() = activityProvider.intent {
        ReleaseActivity.intent(it)
    }

    fun releaseNotesLaunch() = activityProvider.launch {
        val intent = ReleaseActivity.intent(it)
        it.startActivity(intent)
    }
}