package tmg.flashback.ui.settings.appearance.nightmode

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpSize
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
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import tmg.utilities.extensions.toEnum

@Composable
fun SettingsNightModeScreenVM(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: SettingsNightModeViewModel = hiltViewModel(),
) {
    val selected = viewModel.outputs.currentlySelected.collectAsState()

    SettingsNightModeScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
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
    selected: NightMode,
    prefClicked: (Setting.Option) -> Unit
) {
    ScreenView(screenName = "Settings Appearance Night Mode")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_theme_nightmode_title),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_theme_nightmode_title)
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
            actionUpClicked = {},
            prefClicked = {},
            selected = NightMode.DAY
        )
    }
}