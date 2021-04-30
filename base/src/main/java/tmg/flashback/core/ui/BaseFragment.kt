package tmg.flashback.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import org.koin.android.ext.android.inject
import tmg.analytics.controllers.AnalyticsController
import tmg.flashback.core.utils.ScreenAnalytics

abstract class BaseFragment<T: ViewBinding>: Fragment() {

    protected val analyticsController: AnalyticsController by inject()

    private var _binding: T? = null
    protected val binding: T
        get() = _binding!!

    /**
     * Analytics data used in the recording of screen data
     */
    open val screenAnalytics: ScreenAnalytics = ScreenAnalytics()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateView(inflater)
        return binding.root
    }

    abstract fun inflateView(inflater: LayoutInflater): T

    override fun onResume() {
        super.onResume()
        // Commented out due to activities still being used throughout
        recordScreenViewed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Record that a screen has been viewed for analytics
     * @param analytics Instance of the screen analytics we will be reporting. Defaults to class level value
     */
    open fun recordScreenViewed(
        analytics: ScreenAnalytics = screenAnalytics
    ) {
        analyticsController.viewScreen(
            screenName = analytics.screenName ?: this.javaClass.simpleName,
            clazz = this.javaClass,
            params = analytics.attributes
        )
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