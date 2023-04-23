package tmg.flashback.maintenance

import android.content.Context
import android.content.Intent
import tmg.flashback.maintenance.ui.forceupgrade.ForceUpgradeActivity
import tmg.flashback.navigation.ActivityProvider
import javax.inject.Inject

class MaintenanceNavigationComponent @Inject constructor(
    private val activityProvider: ActivityProvider
) {

    private fun forceUpgradeIntent(context: Context): Intent {
        return ForceUpgradeActivity.intent(context)
    }

    fun forceUpgrade() = activityProvider.launch {
        val intent = forceUpgradeIntent(it)
        it.startActivity(intent)
    }


}