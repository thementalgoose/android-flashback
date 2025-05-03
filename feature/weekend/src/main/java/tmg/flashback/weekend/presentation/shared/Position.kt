package tmg.flashback.weekend.presentation.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextTitle

internal val finishingPositionWidth: Dp = 42.dp

@Composable
internal fun Position(
    label: String,
    modifier: Modifier = Modifier
) {
    Box(modifier.width(finishingPositionWidth)) {
        TextTitle(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    horizontal = AppTheme.dimens.xsmall,
                    vertical = AppTheme.dimens.medium
                ),
            bold = true,
            textAlign = TextAlign.Center,
            text = label
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Position("1")
    }
}
@PreviewTheme
@Composable
private fun PreviewMax() {
    AppThemePreview {
        Position("20")
    }
}