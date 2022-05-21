package tmg.flashback.ui.components.layouts

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1

@Composable
fun Container(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isOutlined: Boolean = false,
    boxRadius: Dp = AppTheme.dimensions.radiusSmall,
    content: @Composable BoxScope.() -> Unit
) {
    val backgroundColor = animateColorAsState(targetValue = when (isSelected) {
        true -> AppTheme.colors.primary.copy(alpha = 0.2f)
        false -> if (isOutlined) AppTheme.colors.backgroundSecondary else Color.Transparent
    })

    Box(modifier = modifier
        .clip(RoundedCornerShape(boxRadius))
        .background(backgroundColor.value)
    ) {
        content()
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Column {
            Container(
                modifier = Modifier.fillMaxWidth()
                    .clickable(onClick = { }),
                isSelected = false,
                isOutlined = false
            ) {
                TextBody1(
                    "Item1",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Container(
                modifier = Modifier.fillMaxWidth(),
                isSelected = true,
                isOutlined = false,
            ) {
                TextBody1(
                    "Item2",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Container(
                modifier = Modifier.fillMaxWidth(),
                isSelected = false,
                isOutlined = true,
            ) {
                TextBody1(
                    "Item3",
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Container(
                modifier = Modifier.fillMaxWidth(),
                isOutlined = true,
                isSelected = true
            ) {
                TextBody1(
                    "Item4",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}