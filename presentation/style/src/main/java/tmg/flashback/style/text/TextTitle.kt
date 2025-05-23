package tmg.flashback.style.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextTitle(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    textColor: Color? = null,
    maxLines: Int? = null,
    bold: Boolean = false
) {
    Text(
        text,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines ?: Int.MAX_VALUE,
        style = AppTheme.typography.title.copy(
            fontWeight = when (bold) {
                true -> FontWeight.Bold
                false -> FontWeight.Normal
            },
            color = textColor ?: AppTheme.colors.contentPrimary
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