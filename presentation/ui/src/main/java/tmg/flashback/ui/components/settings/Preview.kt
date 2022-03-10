package tmg.flashback.ui.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppThemePreview

@Preview
@Composable
private fun PreviewLight() {
    AppThemePreview(isLight = true) {
        Column {
            Category(text = "Configure")
            Preference(
                title = "Configure sources",
                subtitle = "Setup which RSS feeds you wish to subscribe too",
                preferenceClicked = { }
            )
            Category(text = "Appearance")
            SwitchPreference(
                title = "Show description",
                subtitle = "Show the article description alongside the RSS source if it has one",
                isChecked = true,
                preferenceClicked = { }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDark() {
    AppThemePreview(isLight = false) {
        Column {
            Category(text = "Configure")
            Preference(
                title = "Configure sources",
                subtitle = "Setup which RSS feeds you wish to subscribe too",
                preferenceClicked = { }
            )
            Category(text = "Appearance")
            SwitchPreference(
                title = "Show description",
                subtitle = "Show the article description alongside the RSS source if it has one",
                isChecked = true,
                preferenceClicked = { }
            )
        }
    }
}