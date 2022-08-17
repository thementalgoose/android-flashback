package tmg.flashback.permissions.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment
import tmg.utilities.extensions.toEnum

@AndroidEntryPoint
internal class RationaleBottomSheetFragment: BaseBottomSheetComposeFragment() {

    private val rationaleType: RationaleType by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(keyRationale, RationaleType::class.java)!!
        } else {
            arguments?.getParcelable(keyRationale)!!
        }
    }

    private val viewModel by viewModels<RationaleViewModel>()

    override val content = @Composable {
        RationaleScreen(
            type = rationaleType,
            cancelClicked = {
                dismissAllowingStateLoss()
            },
            confirmClicked = {
                viewModel.inputs.requestPermission(rationaleType)
            }
        )
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