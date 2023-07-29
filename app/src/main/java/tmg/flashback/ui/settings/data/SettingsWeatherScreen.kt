package tmg.flashback.ui.settings.data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import tmg.flashback.R
import tmg.flashback.style.AppTheme
import tmg.flashback.style.AppThemePreview
import tmg.flashback.style.annotations.PreviewTheme
import tmg.flashback.ui.components.analytics.ScreenView
import tmg.flashback.ui.components.header.HeaderAction
import tmg.flashback.ui.components.settings.Footer
import tmg.flashback.ui.components.settings.Header
import tmg.flashback.ui.components.settings.Switch
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings

@Composable
fun SettingsWeatherScreenVM(
    showBack: Boolean = true,
    actionUpClicked: () -> Unit = { },
    viewModel: SettingsWeatherViewModel = hiltViewModel()
) {
    ScreenView(screenName = "Settings - Weather")

    val temperatureMetric = viewModel.outputs.weatherTemperatureMetric.collectAsState(true)
    val windspeedMetric = viewModel.outputs.weatherWindspeedMetric.collectAsState(false)
    SettingsWeatherScreen(
        showBack = showBack,
        actionUpClicked = actionUpClicked,
        prefClicked = viewModel.inputs::prefClicked,
        temperatureMetric = temperatureMetric.value,
        windspeedMetric = windspeedMetric.value
    )
}

@Composable
fun SettingsWeatherScreen(
    showBack: Boolean,
    actionUpClicked: () -> Unit,
    prefClicked: (Setting) -> Unit,
    temperatureMetric: Boolean,
    windspeedMetric: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backgroundPrimary),
        content = {
            item("header") {
                tmg.flashback.ui.components.header.Header(
                    text = stringResource(id = R.string.settings_header_weather),
                    action = if (showBack) HeaderAction.BACK else null,
                    actionUpClicked = actionUpClicked
                )
            }

            Header(title = R.string.settings_header_weather_metrics)
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
            showBack = true,
            actionUpClicked = {},
            prefClicked = {},
            temperatureMetric = true,
            windspeedMetric = false
        )
    }
}