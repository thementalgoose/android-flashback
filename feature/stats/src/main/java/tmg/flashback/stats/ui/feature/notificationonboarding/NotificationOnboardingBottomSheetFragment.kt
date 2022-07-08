package tmg.flashback.stats.ui.feature.notificationonboarding

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment

class NotificationOnboardingBottomSheetFragment: BaseBottomSheetComposeFragment() {

    override val content = @Composable {
        NotificationOnboardingScreenVM()
    }

    companion object {

        fun instance() = NotificationOnboardingBottomSheetFragment().apply {
            arguments = bundleOf()
        }
    }
}