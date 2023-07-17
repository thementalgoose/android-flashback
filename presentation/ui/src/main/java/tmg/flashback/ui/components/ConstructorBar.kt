package tmg.flashback.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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