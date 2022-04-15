package tmg.flashback.ui.components.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextCaption
import tmg.flashback.ui.R

@Composable
fun ExperimentalLabel(
    modifier: Modifier = Modifier
) {
    TextCaption(
        text = stringResource(id = R.string.settings_experimental),
        modifier = modifier
            .clip(RoundedCornerShape(AppTheme.dimensions.radiusLarge))
            .background(AppTheme.colors.backgroundSecondary)
            .padding(
                start = AppTheme.dimensions.paddingSmall,
                end = AppTheme.dimensions.paddingSmall,
                top = 2.dp,
                bottom = 2.dp
            )
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ExperimentalLabel()
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ExperimentalLabel()
    }
}