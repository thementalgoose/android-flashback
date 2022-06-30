package tmg.flashback.style.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextHeadline1(
    text: String,
    modifier: Modifier = Modifier,
    brand: Boolean = false
) {
    Text(
        text,
        modifier = modifier.fillMaxWidth(),
        style = AppTheme.typography.h1.copy(
            color = when (brand) {
                true -> AppTheme.colors.primary
                false -> AppTheme.colors.contentPrimary
            }
        )
    )
}

@PreviewTheme
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextHeadline1(
            text = "Headline 1"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewBrand() {
    AppThemePreview {
        TextHeadline1(
            text = "Headline 1 Brand",
            brand = true
        )
    }
}