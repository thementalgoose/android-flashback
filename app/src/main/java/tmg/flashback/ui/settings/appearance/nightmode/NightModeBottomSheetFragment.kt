package tmg.flashback.ui.settings.appearance.nightmode

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment
import tmg.flashback.ui.components.analytics.ScreenView

@AndroidEntryPoint
class NightModeBottomSheetFragment: BaseBottomSheetComposeFragment() {
    override val content: @Composable () -> Unit = {
        ScreenView(screenName = "Settings Appearance Night Mode Picker")

        NightModeScreenVM()
    }
}