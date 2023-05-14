package tmg.flashback.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tmg.flashback.analytics.manager.AnalyticsManager
import tmg.flashback.style.AppTheme
import javax.inject.Inject

@Deprecated("Please use ModalSheet(visible = true) instead!")
abstract class BaseBottomSheetComposeFragment: BottomSheetDialogFragment() {

    @Inject
    protected lateinit var analyticsManager: AnalyticsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setShowListener()
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    content.invoke()
                }
            }
        }
    }

    abstract val content: @Composable () -> Unit

    private fun setShowListener() {
        dialog?.setOnShowListener {
            val bsDialog = it as? BottomSheetDialog
            val frameLayout: FrameLayout? = bsDialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet)
            frameLayout?.let { layout ->
                BottomSheetBehavior.from(layout).state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }
    }

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, params, this::class.java)
    }
}