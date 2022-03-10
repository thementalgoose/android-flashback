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
import tmg.flashback.style.text.TextBody1
import tmg.flashback.style.text.TextBody2

@Composable
fun SwitchPreference(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    preferenceClicked: (newState: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = {
                preferenceClicked(isChecked)
            })
            .padding(
                start = AppTheme.dimensions.paddingMedium,
                end = AppTheme.dimensions.paddingMedium,
                top = AppTheme.dimensions.paddingNSmall,
                bottom = AppTheme.dimensions.paddingNSmall
            )
    ) {
        Column(modifier = Modifier
            .weight(1f)
        ) {
            TextBody1(text = title)
            Spacer(modifier = Modifier.height(4.dp))
            TextBody2(text = subtitle)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Checkbox(
            checked = isChecked,
            onCheckedChange = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        SwitchPreference(
            title = "Show description",
            subtitle = "Show the article description alongside the rss source if it has one",
            isChecked = true,
            preferenceClicked = { }
        )
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        SwitchPreference(
            title = "Show description",
            subtitle = "Show the article description alongside the rss source if it has one",
            isChecked = true,
            preferenceClicked = { }
        )
    }
}

