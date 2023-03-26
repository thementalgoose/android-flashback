package tmg.flashback.results.ui.settings.notifications.reminder

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment
import tmg.flashback.ui.components.analytics.ScreenView

@AndroidEntryPoint
class UpNextReminderBottomSheetFragment: BaseBottomSheetComposeFragment() {

    override val content: @Composable () -> Unit = {
        ScreenView(screenName = "Up Next Reminders")

        UpNextReminderScreenVM(
            dismiss = ::dismiss
        )
    }
}