package tmg.flashback.ui.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection

@Composable
fun PaddingValues.asInsets(): WindowInsets {
    val layoutDirection = LocalLayoutDirection.current
    return WindowInsets(
        left = this.calculateLeftPadding(layoutDirection),
        right = this.calculateRightPadding(layoutDirection),
        top = this.calculateTopPadding(),
        bottom = this.calculateBottomPadding()
    )
}