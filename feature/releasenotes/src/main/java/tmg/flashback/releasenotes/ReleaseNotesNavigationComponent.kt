package tmg.flashback.releasenotes

import android.content.Context
import android.content.Intent
import tmg.flashback.releasenotes.ui.releasenotes.ReleaseActivity
import tmg.flashback.ui.navigation.ActivityProvider

class ReleaseNotesNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    internal fun releaseNotesIntent(context: Context): Intent {
        return ReleaseActivity.intent(context)
    }

    fun releaseNotes() = activityProvider.launch {
        val intent = releaseNotesIntent(it)
        it.startActivity(intent)
    }
}