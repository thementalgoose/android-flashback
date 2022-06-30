package tmg.flashback.ui.components.messages

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2


@Composable
fun Message(
    title: String,
    modifier: Modifier = Modifier
) {
    TextBody2(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
        text = title,
        bold = true
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Message(
            title = "Data is provided with <3 by the Flashback team and Ergast API"
        )
    }
}