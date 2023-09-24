package tmg.flashback.ui.permissions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.device.AppPermissions
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment

@AndroidEntryPoint
internal class RationaleBottomSheetFragment: BaseBottomSheetComposeFragment() {

    private var callback: RationaleBottomSheetFragmentCallback? = null

    private val rationaleType: Array<String> by lazy {
        arguments?.getStringArray(keyRationale)!!
    }
    private val appPermissions: List<AppPermissions.RuntimePermission> by lazy {
        AppPermissions.RuntimePermission.get(rationaleType.toList())
    }

    override val content = @Composable {
        RationaleScreen(
            type = appPermissions,
            cancelClicked = {
                callback?.rationaleCancelClicked(appPermissions)
                dismissAllowingStateLoss()
            },
            confirmClicked = {
                callback?.rationaleConfirmClicked(appPermissions)
                dismissAllowingStateLoss()
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            callback = context
        }
    }

    companion object {
        private const val keyRationale: String = "rationale"

        fun instance(rationaleType: Array<String>): RationaleBottomSheetFragment {
            return RationaleBottomSheetFragment().apply {
                arguments = bundleOf(keyRationale to rationaleType)
            }
        }
    }
}