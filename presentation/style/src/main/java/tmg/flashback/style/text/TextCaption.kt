package tmg.flashback.style.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextCaption(
    text: String,
    modifier: Modifier = Modifier,
    fontStyle: FontStyle = FontStyle.Normal,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text,
        maxLines = maxLines,
        textAlign = textAlign,
        modifier = modifier,
        style = AppTheme.typography.caption.copy(
            fontStyle = fontStyle,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.contentTertiary
        )
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        TextCaption(
            text = "Caption"
        )
    }
}