package tmg.flashback.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tmg.flashback.analytics.manager.AnalyticsManager
import javax.inject.Inject

abstract class BaseBottomSheetFragment<T: ViewBinding>: BottomSheetDialogFragment() {

    lateinit var binding: T

    @Inject
    protected lateinit var analyticsManager: AnalyticsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflateView(inflater)
        return binding.root
    }

    abstract fun inflateView(inflater: LayoutInflater): T

    /**
     * Logging screen analytics
     */
    fun logScreenViewed(name: String, params: Map<String, String> = mapOf()) {
        analyticsManager.viewScreen(name, params, this::class.java)
    }

    val behavior: BottomSheetBehavior<*>
        get() {
            val dialog = dialog as BottomSheetDialog
            return dialog.behavior
        }
}