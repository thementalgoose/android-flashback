package tmg.flashback.weekend.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.formula1.model.Constructor
import tmg.flashback.providers.ConstructorProvider
import tmg.flashback.style.AppThemePreview

internal val constructorIndicatorWidth: Dp = 6.dp

@Composable
internal fun ConstructorIndicator(
    constructor: Constructor
) {
    Box(modifier = Modifier
        .width(constructorIndicatorWidth)
        .fillMaxHeight()
        .background(constructor.colour)
    )
}

@Preview
@Composable
private fun Preview(
    @PreviewParameter(ConstructorProvider::class) constructor: Constructor
) {
    AppThemePreview {
        ConstructorIndicator(constructor = constructor)
    }
}