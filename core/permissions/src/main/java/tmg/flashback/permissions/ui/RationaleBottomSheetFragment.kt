package tmg.flashback.permissions.ui

import androidx.compose.runtime.Composable
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment

class RationaleBottomSheetFragment: BaseBottomSheetComposeFragment() {

    override val content = @Composable {
        RationaleScreen(
            type = RationaleType.RUNTIME_NOTIFICATIONS,
            cancelClicked = { },
            confirmClicked = { }
        )
    }
}