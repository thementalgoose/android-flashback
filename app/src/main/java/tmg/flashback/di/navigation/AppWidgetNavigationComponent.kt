package tmg.flashback.di.navigation

import android.content.Context
import android.content.Intent
import tmg.flashback.googleanalytics.manager.FirebaseAnalyticsManager
import tmg.flashback.ui.HomeActivity
import tmg.flashback.widgets.contract.WidgetNavigationComponent
import javax.inject.Inject

class AppWidgetNavigationComponent @Inject constructor(
    private val firebaseAnalyticsManager: FirebaseAnalyticsManager
): WidgetNavigationComponent {

    override fun getLaunchAppIntent(context: Context): Intent {
        firebaseAnalyticsManager.logEvent("relaunch_app")
        return Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}