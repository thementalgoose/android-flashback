package tmg.flashback.forceupgrade

import tmg.flashback.forceupgrade.ui.forceupgrade.ForceUpgradeActivity
import tmg.flashback.ui.navigation.ActivityProvider

class ForceUpgradeNavigationComponent(
    private val activityProvider: ActivityProvider
) {

    private fun forceUpgradeIntent() = activityProvider.intent {
        ForceUpgradeActivity.intent(it)
    }

    fun forceUpgradeLaunch() = activityProvider.launch {
        val intent = ForceUpgradeActivity.intent(it)
        it.startActivity(intent)
    }
}