package tmg.flashback.presentation.settings.appearance.nightmode

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
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.settings.Setting
import tmg.utilities.extensions.toEnum

@Composable
fun SettingsNightModeScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    viewModel: SettingsNightModeViewModel = hiltViewModel(),
) {
    val selected = viewModel.outputs.currentlySelected.collectAsState()

    SettingsNightModeScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        paddingValues = paddingValues,
        selected = selected.value,
        prefClicked = { option ->
            val value = option.key.toEnum<NightMode> { it.key }!!
            viewModel.inputs.selectNightMode(value)
        }
    )
}

@Composable
fun SettingsNightModeScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    selected: NightMode,
    prefClicked: (Setting.Option) -> Unit
) {
    ScreenView(screenName = "Settings Appearance Night Mode")

    LazyColumn(
        contentPadding = paddingValues,
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = string.settings_theme_nightmode_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_theme_nightmode_title)
            Option(
                model = Settings.Theme.darkModeOption(type = NightMode.DEFAULT, isChecked = selected == NightMode.DEFAULT),
                onClick = prefClicked
            )
            Option(
                model = Settings.Theme.darkModeOption(type = NightMode.DAY, isChecked = selected == NightMode.DAY),
                onClick = prefClicked
            )
            Option(
                model = Settings.Theme.darkModeOption(type = NightMode.NIGHT, isChecked = selected == NightMode.NIGHT),
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
        SettingsNightModeScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            paddingValues = PaddingValues.Absolute(),
            actionUpClicked = {},
            prefClicked = {},
            selected = NightMode.DAY
        )
    }
}