package tmg.flashback.ui.components.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1

@Composable
fun Container(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.backgroundPrimary,
    internalPadding: Dp = AppTheme.dimensions.paddingMedium,
    externalPadding: Dp = AppTheme.dimensions.paddingMedium,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier.padding(
        vertical = externalPadding,
        horizontal = externalPadding
    )) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusSmall))
            .background(backgroundColor)
            .padding(
                vertical = internalPadding,
                horizontal = internalPadding
            ),
            content = content
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Container {
            TextBody1("Hey")
        }
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Container {
            TextBody1("Hey")
        }
    }
}