package tmg.flashback.style.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

@Composable
fun TextHeadline2(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
    brand: Boolean = false
) {
    Text(
        text,
        modifier = modifier
            .fillMaxWidth(),
        maxLines = maxLines,
        style = AppTheme.typography.h2.copy(
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
        TextHeadline2(
            text = "Headline 2"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextHeadline2(
            text = "Headline 2"
        )
    }
}

@Preview
@Composable
private fun PreviewLightBrand() {
    AppThemePreview(isLight = true) {
        TextHeadline2(
            text = "Headline 2 Brand",
            brand = true
        )
    }
}

@Preview
@Composable
private fun PreviewDarkBrand() {
    AppThemePreview(isLight = false) {
        TextHeadline2(
            text = "Headline 2 Brand",
            brand = true
        )
    }
}