package tmg.flashback.style.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

@Composable
fun TextTitle(
    text: String,
    bold: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        modifier = modifier,
        style = AppTheme.typography.title.copy(
            fontWeight = when (bold) {
                true -> FontWeight.Bold
                false -> FontWeight.Normal
            },
            color = AppTheme.colors.contentPrimary
        )
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextTitle(
            text = "Title"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextTitle(
            text = "Title"
        )
    }
}

@Preview
@Composable
private fun PreviewLightBold() {
    AppThemePreview(isLight = true) {
        TextTitle(
            text = "Title Bold",
            bold = true
        )
    }
}

@Preview
@Composable
private fun PreviewDarkBold() {
    AppThemePreview(isLight = false) {
        TextTitle(
            text = "Title Bold",
            bold = true
        )
    }
}