package tmg.flashback.presentation.settings.appearance.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.googleanalytics.presentation.ScreenView
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.strings.R.string
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Option
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.settings.Setting
import tmg.utilities.extensions.toEnum

@Composable
fun SettingsThemeScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    viewModel: SettingsThemeViewModel = hiltViewModel(),
) {
    val selected = viewModel.outputs.currentlySelected.collectAsState()

    SettingsThemeScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        paddingValues = paddingValues,
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
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    selected: Theme,
    prefClicked: (Setting.Option) -> Unit
) {
    ScreenView(screenName = "Settings Appearance Theme")

    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = string.settings_theme_theme_title),
                    action = if (windowSizeClass.widthSizeClass != WindowWidthSizeClass.Expanded) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_theme_theme_title)
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
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            paddingValues = PaddingValues.Absolute(),
            actionUpClicked = {},
            prefClicked = {},
            selected = Theme.MATERIAL_YOU
        )
    }
}