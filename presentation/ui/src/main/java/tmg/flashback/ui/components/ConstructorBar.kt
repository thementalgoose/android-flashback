package tmg.flashback.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

fun Modifier.constructorIndicator(
    colour: Color,
    size: Dp = 6.dp
): Modifier {
    return this
        .drawBehind {
            this.drawRect(
                color = colour,
                size = Size(size.toPx(), this.size.height)
            )
        }
        .padding(start = size)
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview {
        Box(modifier = Modifier
            .size(width = 50.dp, height = 16.dp)
            .background(Color.White)
            .constructorIndicator(AppTheme.colors.primary)
        )
    }
}