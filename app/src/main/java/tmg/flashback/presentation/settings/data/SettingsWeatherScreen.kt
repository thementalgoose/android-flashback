package tmg.flashback.presentation.settings.data

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting

@Composable
fun SettingsWeatherScreenVM(
    actionUpClicked: () -> Unit = { },
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    viewModel: SettingsWeatherViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Weather")

    val temperatureMetric = viewModel.outputs.weatherTemperatureMetric.collectAsState(true)
    val windspeedMetric = viewModel.outputs.weatherWindspeedMetric.collectAsState(false)
    SettingsWeatherScreen(
        actionUpClicked = actionUpClicked,
        windowSizeClass = windowSizeClass,
        paddingValues = paddingValues,
        prefClicked = viewModel.inputs::prefClicked,
        temperatureMetric = temperatureMetric.value,
        windspeedMetric = windspeedMetric.value
    )
}

@Composable
fun SettingsWeatherScreen(
    actionUpClicked: () -> Unit,
    windowSizeClass: WindowSizeClass,
    paddingValues: PaddingValues,
    prefClicked: (Setting) -> Unit,
    temperatureMetric: Boolean,
    windspeedMetric: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = paddingValues,
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = string.settings_header_weather),
                    action = if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = string.settings_header_weather_metrics)
            Switch(
                model = Settings.Data.temperatureUnits(temperatureMetric),
                onClick = prefClicked
            )
            Switch(
                model = Settings.Data.windSpeedUnits(windspeedMetric),
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
        SettingsWeatherScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
            paddingValues = PaddingValues.Absolute(),
            actionUpClicked = {},
            prefClicked = {},
            temperatureMetric = true,
            windspeedMetric = false
        )
    }
}