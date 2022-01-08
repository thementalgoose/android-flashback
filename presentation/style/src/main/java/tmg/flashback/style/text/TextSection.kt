package tmg.flashback.style.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

@Composable
fun TextSection(
    text: String,
    brand: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        modifier = modifier,
        style = AppTheme.typography.section.copy(
            color = when (brand) {
                true -> AppTheme.colors.primary
                false -> AppTheme.colors.contentSecondary
            }
        )
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextSection(
            text = "Section"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextSection(
            text = "Section"
        )
    }
}

@Preview
@Composable
private fun PreviewLightBrand() {
    AppThemePreview(isLight = true) {
        TextSection(
            text = "Section Brand",
            brand = true
        )
    }
}

@Preview
@Composable
private fun PreviewDarkBrand() {
    AppThemePreview(isLight = false) {
        TextSection(
            text = "Section Brand",
            brand = true
        )
    }
}