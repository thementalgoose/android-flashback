package tmg.flashback.results.ui.feature.notificationonboarding

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment

@AndroidEntryPoint
class NotificationOnboardingBottomSheetFragment: BaseBottomSheetComposeFragment() {

    override val content = @Composable {
        NotificationOnboardingScreenVM(
            actionUpClicked = {
                this.dismiss()
            }
        )
    }

    companion object {

        fun instance() = NotificationOnboardingBottomSheetFragment().apply {
            arguments = bundleOf()
        }
    }
}