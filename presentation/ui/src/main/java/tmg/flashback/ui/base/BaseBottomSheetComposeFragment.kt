package tmg.flashback.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.style.AppTheme
import javax.inject.Inject

abstract class BaseBottomSheetComposeFragment: BottomSheetDialogFragment() {

    @Inject
    protected lateinit var analyticsManager: AnalyticsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    content.invoke()
                }
            }
        }
    }

    abstract val content: @Composable () -> Unit

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, params, this::class.java)
    }
}