package tmg.flashback.style.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

@Composable
fun TextHeadline1(
    text: String,
    brand: Boolean = false,
    modifier: Modifier = Modifier
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

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextHeadline1(
            text = "Headline 1"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextHeadline1(
            text = "Headline 1"
        )
    }
}

@Preview
@Composable
private fun PreviewLightBrand() {
    AppThemePreview(isLight = true) {
        TextHeadline1(
            text = "Headline 1 Brand",
            brand = true
        )
    }
}

@Preview
@Composable
private fun PreviewDarkBrand() {
    AppThemePreview(isLight = false) {
        TextHeadline1(
            text = "Headline 1 Brand",
            brand = false
        )
    }
}