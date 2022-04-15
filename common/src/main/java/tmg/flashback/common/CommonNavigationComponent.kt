package tmg.flashback.common

import android.content.Intent
import tmg.flashback.common.ui.forceupgrade.ForceUpgradeActivity
import tmg.flashback.common.ui.privacypolicy.PrivacyPolicyActivity
import tmg.flashback.common.ui.releasenotes.ReleaseActivity
import tmg.flashback.ui.navigation.ActivityProvider

class CommonNavigationComponent(
    private val activityProvider: ActivityProvider
) {
    private fun releaseNotesIntent() = activityProvider.intent {
        ReleaseActivity.intent(it)
    }

    fun releaseNotesLaunch() = activityProvider.launch {
        val intent = ReleaseActivity.intent(it)
        it.startActivity(intent)
    }

    private fun privacyPolicyIntent() = activityProvider.intent {
        PrivacyPolicyActivity.intent(it)
    }

    fun privacyPolicyLaunch() = activityProvider.launch {
        val intent = PrivacyPolicyActivity.intent(it)
        it.startActivity(intent)
    }

    private fun forceUpgradeIntent() = activityProvider.intent {
        ForceUpgradeActivity.intent(it)
    }

    fun forceUpgradeLaunch() = activityProvider.launch {
        val intent = ForceUpgradeActivity.intent(it)
        it.startActivity(intent)
    }
}