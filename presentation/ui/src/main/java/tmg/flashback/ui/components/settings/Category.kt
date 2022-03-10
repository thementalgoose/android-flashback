package tmg.flashback.ui.components.settings

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.SupportedTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.ui.model.Theme

@Composable
fun Category(
    text: String,
    modifier: Modifier = Modifier
) {
    TextBody1(
        text = text,
        bold = true,
        textColor = AppTheme.colors.primary,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingXSmall,
                bottom = AppTheme.dimensions.paddingXSmall
            )
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Category(text = "Appearance")
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Column {
            Category(text = "Appearance")
        }
    }
}