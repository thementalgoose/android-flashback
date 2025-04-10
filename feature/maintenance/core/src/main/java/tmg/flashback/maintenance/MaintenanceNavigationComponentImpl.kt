package tmg.flashback.maintenance

import android.content.Context
import android.content.Intent
import tmg.flashback.maintenance.contract.MaintenanceNavigationComponent
import tmg.flashback.maintenance.presentation.forceupgrade.ForceUpgradeActivity
import tmg.flashback.device.ActivityProvider
import javax.inject.Inject

internal class MaintenanceNavigationComponentImpl @Inject constructor(
    private val activityProvider: ActivityProvider
): MaintenanceNavigationComponent {

    private fun forceUpgradeIntent(context: Context): Intent {
        return ForceUpgradeActivity.intent(context)
    }

    override fun forceUpgrade() = activityProvider.launch {
        val intent = forceUpgradeIntent(it)
        it.startActivity(intent)
    }


}