package tmg.core.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.koin.android.ext.android.inject
import tmg.core.analytics.manager.AnalyticsManager
import tmg.crash_reporting.controllers.CrashController

abstract class BaseFragment<T: ViewBinding>: Fragment() {

    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    protected val analyticsManager: AnalyticsManager by inject()

    protected val crashManager: CrashController by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateView(inflater)
        return binding.root
    }

    abstract fun inflateView(inflater: LayoutInflater): T

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, this::class.java, params)
    }

    /**
     * Callable from the super class to get window insets
     *  based on the binding
     */
    fun setInsets(callback: (insets: WindowInsetsCompat) -> Unit) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            callback(insets)
            insets
        }
    }
}