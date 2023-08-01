package tmg.flashback.ui.settings.appearance.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Option
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import tmg.flashback.ui.settings.appearance.nightmode.SettingsNightModeViewModel
import tmg.utilities.extensions.toEnum

@Composable
fun SettingsThemeScreenVM(
    actionUpClicked: () -> Unit,
    viewModel: SettingsThemeViewModel = hiltViewModel(),
) {
    val selected = viewModel.outputs.currentlySelected.collectAsState()

    SettingsThemeScreen(
        actionUpClicked = actionUpClicked,
        selected = selected.value,
        prefClicked = { option ->
            val value = option.key.toEnum<Theme> { it.key }!!
            viewModel.inputs.selectTheme(value)
        }
    )
}

@Composable
fun SettingsThemeScreen(
    actionUpClicked: () -> Unit,
    selected: Theme,
    prefClicked: (Setting.Option) -> Unit
) {
    ScreenView(screenName = "Settings Appearance Theme")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_theme_theme_title),
                    action = HeaderAction.BACK,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_theme_theme_title)
            Option(
                model = Settings.Theme.themeOption(type = Theme.DEFAULT, isChecked = selected == Theme.DEFAULT),
                onClick = prefClicked
            )
            Option(
                model = Settings.Theme.themeOption(type = Theme.MATERIAL_YOU, isChecked = selected == Theme.MATERIAL_YOU),
                onClick = prefClicked
            )

            Footer()
        }
    )
}

@PreviewTheme
@Composable
private fun Preview() {
    AppThemePreview {
        SettingsThemeScreen(
            actionUpClicked = {},
            prefClicked = {},
            selected = Theme.MATERIAL_YOU
        )
    }
}