package tmg.flashback.forceupgrade

import android.content.Context
import android.content.Intent
import tmg.flashback.forceupgrade.ui.forceupgrade.ForceUpgradeActivity
import tmg.flashback.ui.navigation.ActivityProvider
import javax.inject.Inject

class ForceUpgradeNavigationComponent @Inject constructor(
    private val activityProvider: ActivityProvider
) {

    fun forceUpgradeIntent(context: Context): Intent {
        return ForceUpgradeActivity.intent(context)
    }

    fun forceUpgrade() = activityProvider.launch {
        val intent = forceUpgradeIntent(it)
        it.startActivity(intent)
    }
}