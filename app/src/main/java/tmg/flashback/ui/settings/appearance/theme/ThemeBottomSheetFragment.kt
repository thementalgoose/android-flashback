package tmg.flashback.ui.settings.appearance.theme

import androidx.compose.runtime.Composable
import dagger.hilt.android.AndroidEntryPoint
import tmg.flashback.ui.base.BaseBottomSheetComposeFragment
import tmg.flashback.ui.components.analytics.ScreenView

@AndroidEntryPoint
class ThemeBottomSheetFragment: BaseBottomSheetComposeFragment() {
    override val content: @Composable () -> Unit = {
        ScreenView(screenName = "Settings Appearance Color Theme Picker")

        ThemeScreenVM()
    }
}