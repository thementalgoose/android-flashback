package tmg.flashback.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.koin.android.ext.android.inject
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.crash_reporting.controllers.CrashController

abstract class BaseFragment: Fragment() {

    protected val analyticsManager: AnalyticsManager by inject()

    protected val crashManager: CrashController by inject()

    abstract fun onCreateView(): View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return onCreateView()
    }

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, this::class.java, params)
    }
}