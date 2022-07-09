package tmg.flashback.di.navigation

import android.content.Context
import android.content.Intent
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.ui.HomeActivity
import tmg.flashback.widgets.WidgetNavigationComponent

class AppWidgetNavigationComponent(
    private val analyticsManager: AnalyticsManager
): WidgetNavigationComponent {
    override fun launchApp(context: Context): Intent {
        analyticsManager.logEvent("relaunch_app")
        return Intent(context, HomeActivity::class.java)
    }
}