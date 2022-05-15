package tmg.flashback.stats.ui.messaging

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.inject
import tmg.flashback.stats.repository.HomeRepository
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.text.TextBody2
import tmg.flashback.ui.navigation.ApplicationNavigationComponent

@Composable
fun ProvidedBy(
    modifier: Modifier = Modifier
) {
    val homeRepository: HomeRepository by inject()
    val message = homeRepository.dataProvidedBy ?: return

    val navigationComponent: ApplicationNavigationComponent by inject()

    ProvidedBy(
        modifier = modifier,
        title = message,
        clicked = {
            navigationComponent.aboutApp()
        }
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
                horizontal = AppTheme.dimensions.paddingMedium,
                vertical = AppTheme.dimensions.paddingSmall
            ),
        text = title,
        bold = true
    )
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        ProvidedBy(
            title = "Data is provided with <3 by the Flashback team and Ergast API",
            clicked = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        ProvidedBy(
            title = "Data is provided with <3 by the Flashback team and Ergast API",
            clicked = {}
        )
    }
}