package tmg.flashback.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
internal fun SwitchPreference(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    preferenceClicked: (newState: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    beta: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {
                preferenceClicked(isChecked)
            })
            .padding(
                start = AppTheme.dimensions.paddingSmall, // Experimental label
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            TextBody1(
                text = title,
                modifier = Modifier.padding(start = AppTheme.dimensions.paddingSmall)
            )
            Spacer(modifier = Modifier.height(4.dp))
            TextBody2(
                text = subtitle,
                modifier = Modifier.padding(start = AppTheme.dimensions.paddingSmall)
            )
            if (beta) {
                Spacer(modifier = Modifier.height(4.dp))
                ExperimentalLabel()
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Checkbox(
            checked = isChecked,
            onCheckedChange = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SwitchPreference(
            title = "Show description",
            subtitle = "Show the article description alongside the rss source if it has one",
            isChecked = true,
            preferenceClicked = { }
        )
    }
}

