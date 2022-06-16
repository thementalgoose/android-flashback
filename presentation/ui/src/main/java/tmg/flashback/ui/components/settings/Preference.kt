package tmg.flashback.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
internal fun Preference(
    title: String,
    subtitle: String,
    preferenceClicked: () -> Unit,
    modifier: Modifier = Modifier,
    beta: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = preferenceClicked)
            .padding(
                start = AppTheme.dimensions.paddingSmall, // Experimental label
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        TextBody1(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = AppTheme.dimensions.paddingSmall)
        )
        Spacer(modifier = Modifier.height(4.dp))
        TextBody2(
            text = subtitle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = AppTheme.dimensions.paddingSmall)
        )
        if (beta) {
            Spacer(modifier = Modifier.height(4.dp))
            ExperimentalLabel()
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        Preference(
            title = "App theme",
            subtitle = "Pick your app colour scheme",
            beta = true,
            preferenceClicked = { }
        )
    }
}