package tmg.flashback.statistics.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextTitle

@Composable
fun TextRanking(
    position: Int?
) {
    Box {
        TextTitle(
            text = position?.toString() ?: "-",
            bold = true,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(42.dp)
                .align(Alignment.TopCenter)
                .padding(
                    vertical = AppTheme.dimensions.paddingMedium
                )
        )
    }
}

@Preview
@Composable
private fun Preview() {
    AppThemePreview(isLight = true) {
        TextRanking(position = 1)
    }
}