package tmg.flashback.ui.foldables

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowLayoutInfo

@Composable
fun WindowLayoutInfo.getFoldingConfig(): FoldingConfig? {
    // Is folding
    val foldingFeature = this.displayFeatures
        .firstNotNullOfOrNull { it as? FoldingFeature } ?: return null

    // Is landscape
    if (foldingFeature.orientation == FoldingFeature.Orientation.HORIZONTAL) {
        return null
    }

    // Set width of menu
    val width = with(LocalDensity.current) { foldingFeature.bounds.left.toDp() }
    return FoldingConfig(
        overrideWidth = width ,
        state = foldingFeature.state
    )
}

data class FoldingConfig(
    val overrideWidth: Dp?,
    val state: FoldingFeature.State
)