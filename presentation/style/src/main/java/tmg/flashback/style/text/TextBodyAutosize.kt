package tmg.flashback.style.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme

@Composable
fun TextBodyAutosize(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    textColor: Color? = null,
    maxLines: Int? = null,
    maxTextSize: TextUnit = 36.sp,
) {
    TextBodyAutosize(
        annotatedString = AnnotatedString(text),
        modifier = modifier,
        bold = bold,
        textColor = textColor,
        maxLines = maxLines,
        maxTextSize = maxTextSize,
    )
}

@Composable
fun TextBodyAutosize(
    annotatedString: AnnotatedString,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    textColor: Color? = null,
    maxLines: Int? = null,
    maxTextSize: TextUnit = 36.sp,
) {
    Box(modifier = modifier) {
        BasicText(
            text = annotatedString,
            modifier = Modifier,
            maxLines = maxLines ?: Int.MAX_VALUE,
            overflow = if (maxLines != null) TextOverflow.Ellipsis else TextOverflow.Clip,
            style = AppTheme.typography.body2.copy(
                fontWeight = when (bold) {
                    true -> FontWeight.Bold
                    false -> FontWeight.Normal
                },
                color = textColor ?: AppTheme.colors.contentSecondary
            ),
            autoSize = TextAutoSize.StepBased(minFontSize = 8.sp, maxFontSize = maxTextSize, stepSize = 2.sp)
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewSmall() {
    AppThemePreview {
        TextBodyAutosize(
            modifier = Modifier.width(30.dp).height(50.dp),
            text = "Autosize"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewMedium() {
    AppThemePreview {
        TextBodyAutosize(
            modifier = Modifier.width(50.dp).height(50.dp),
            text = "Autosize"
        )
    }
}

@PreviewTheme
@Composable
private fun PreviewLarge() {
    AppThemePreview {
        TextBodyAutosize(
            modifier = Modifier.width(100.dp).height(50.dp),
            text = "Autosize"
        )
    }
}