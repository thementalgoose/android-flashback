package tmg.flashback.ui.permissions

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ui.base.BaseActivity
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment

@AndroidEntryPoint
internal class RationaleBottomSheetFragment: BaseBottomSheetComposeFragment() {

    var callback: RationaleBottomSheetFragmentCallback? = null

    private val rationaleType: RationaleType by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(keyRationale, RationaleType::class.java)!!
        } else {
            arguments?.getParcelable(keyRationale)!!
        }
    }

    override val content = @Composable {
        RationaleScreen(
            type = rationaleType,
            cancelClicked = {
                callback?.rationaleCancelClicked(rationaleType)
                dismissAllowingStateLoss()
            },
            confirmClicked = {
                callback?.rationaleConfirmClicked(rationaleType)
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

        fun instance(rationaleType: RationaleType): RationaleBottomSheetFragment {
            return RationaleBottomSheetFragment().apply {
                arguments = bundleOf(keyRationale to rationaleType)
            }
        }
    }
}