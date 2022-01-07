package tmg.flashback.style.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview

@Composable
fun TextCaption(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        modifier = modifier,
        style = AppTheme.typography.caption.copy(
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.contentTertiary
        )
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        TextCaption(
            text = "Caption"
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        TextCaption(
            text = "Caption"
        )
    }
}