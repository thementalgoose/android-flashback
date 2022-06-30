package tmg.flashback.style.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextSection(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    brand: Boolean = false
) {
    Text(
        text,
        modifier = modifier,
        textAlign = textAlign,
        style = AppTheme.typography.section.copy(
            color = when (brand) {
                true -> AppTheme.colors.primary
                false -> AppTheme.colors.contentSecondary
            }
        )
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TextSection(
            text = "Section"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewBrand() {
    AppThemePreview {
        TextSection(
            text = "Section Brand",
            brand = true
        )
    }
}