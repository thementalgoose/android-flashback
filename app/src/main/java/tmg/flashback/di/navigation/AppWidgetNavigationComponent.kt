package tmg.flashback.di.navigation

import android.content.Context
import android.content.Intent
import tmg.flashback.googleanalytics.manager.AnalyticsManager
import tmg.flashback.ui.HomeActivity
import tmg.flashback.widgets.contract.WidgetNavigationComponent
import javax.inject.Inject

class AppWidgetNavigationComponent @Inject constructor(
    private val analyticsManager: AnalyticsManager
): WidgetNavigationComponent {

    override fun getLaunchAppIntent(context: Context): Intent {
        analyticsManager.logEvent("relaunch_app")
        return Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}