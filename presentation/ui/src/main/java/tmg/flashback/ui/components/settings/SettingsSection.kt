package tmg.flashback.ui.components.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.R
import tmg.flashback.ui.settings.SettingsModel

@Composable
fun SettingsSection(
    models: List<SettingsModel>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        for (x in models) {
            when (x) {
                is SettingsModel.Header -> {
                    Category(
                        text = stringResource(id = x.title))
                }
                is SettingsModel.Pref -> {
                    Preference(
                        title = stringResource(id = x.title),
                        subtitle = stringResource(id = x.description),
                        preferenceClicked = { x.onClick?.invoke() }
                    )
                }
                is SettingsModel.SwitchPref -> {
                    SwitchPreference(
                        title = stringResource(id = x.title),
                        subtitle = stringResource(id = x.description),
                        isChecked = x.getState(),
                        preferenceClicked = {
                            x.saveState(it)
                            x.saveStateNotification?.invoke(it)
                        }
                    )
                }
            }
        }
    }
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview(isLight = true) {
        SettingsSection(models = fakeSettings)
    }
}

private val fakeSettings = listOf(
    SettingsModel.Header(
        title = R.string.settings_theme_title,
        beta = true
    ),
    SettingsModel.Pref(
        title = R.string.settings_theme_title,
        description = R.string.settings_theme_theme_description,
        onClick = { },
        beta = false
    ),
    SettingsModel.SwitchPref(
        title = R.string.settings_theme_title,
        description = R.string.settings_theme_theme_description,
        getState = { true },
        saveState = { },
        saveStateNotification = { },
        beta = false
    )
)