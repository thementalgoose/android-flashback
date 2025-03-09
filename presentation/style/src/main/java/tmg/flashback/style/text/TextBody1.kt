package tmg.flashback.style.text

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextBody1(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    bold: Boolean = false,
    textColor: Color? = null,
    maxLines: Int? = null
) {
    Text(
        text,
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines ?: Int.MAX_VALUE,
        overflow = if (maxLines != null) TextOverflow.Ellipsis else TextOverflow.Clip,
        style = AppTheme.typography.body1.copy(
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
        TextBody1(
            text = "Body 1"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewBold() {
    AppThemePreview {
        TextBody1(
            text = "Body 1 Bold",
            bold = true
        )
    }
}