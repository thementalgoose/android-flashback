package tmg.flashback.season.presentation.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody2

@Composable
fun ProvidedBy(
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<ProvidedByViewModel>()
    val message = viewModel.message ?: return

    ProvidedBy(
        modifier = modifier,
        title = message,
        clicked = viewModel::navigateToAboutApp
    )
}

@Composable
private fun ProvidedBy(
    title: String,
    clicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextBody2(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = clicked)
            .padding(
                horizontal = AppTheme.dimens.medium,
                vertical = AppTheme.dimens.small
            ),
        text = title,
        bold = true
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        ProvidedBy(
            title = "Data is provided with <3 by the Flashback team",
            clicked = {}
        )
    }
}