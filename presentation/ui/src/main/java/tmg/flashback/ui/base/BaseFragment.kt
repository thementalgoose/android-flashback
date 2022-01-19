package tmg.flashback.ui.base

import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.crash_reporting.controllers.CrashController

abstract class BaseFragment: Fragment() {

    protected val analyticsManager: AnalyticsManager by inject()

    protected val crashManager: CrashController by inject()

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, this::class.java, params)
    }
}