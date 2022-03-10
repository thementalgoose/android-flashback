package tmg.flashback.ui.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody1

@Composable
internal fun Category(
    text: String,
    modifier: Modifier = Modifier,
    beta: Boolean = false,
) {
    Row(modifier = Modifier.padding(top = AppTheme.dimensions.paddingSmall)) {
        TextBody1(
            text = text,
            bold = true,
            textColor = AppTheme.colors.primary,
            modifier = modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
                .padding(
                    start = AppTheme.dimensions.paddingMedium,
                    end = AppTheme.dimensions.paddingMedium,
                    top = AppTheme.dimensions.paddingXSmall,
                    bottom = AppTheme.dimensions.paddingXSmall
                )
        )
        if (beta) {
            ExperimentalLabel(
                modifier = Modifier.align(CenterVertically)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Category(
            text = "Appearance",
            beta = true
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Column {
            Category(
                text = "Appearance"
            )
        }
    }
}