package tmg.flashback.style.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    bold: Boolean = false
) {
    Text(
        text,
        modifier = modifier,
        textAlign = textAlign,
        style = AppTheme.typography.title.copy(
            fontWeight = when (bold) {
                true -> FontWeight.Bold
                false -> FontWeight.Normal
            },
            color = AppTheme.colors.contentPrimary
        )
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TextTitle(
            text = "Title"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewBold() {
    AppThemePreview(isLight = true) {
        TextTitle(
            text = "Title Bold",
            bold = true
        )
    }
}