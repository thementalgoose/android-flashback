package tmg.flashback.presentation.settings.data

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tmg.flashback.ui.settings.Setting
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.weekend.repository.WeatherRepository
import javax.inject.Inject

interface SettingsWeatherViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsWeatherViewModelOutputs {
    val weatherTemperatureMetric: StateFlow<Boolean>
    val weatherWindspeedMetric: StateFlow<Boolean>
}

@HiltViewModel
class SettingsWeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel(), SettingsWeatherViewModelInputs, SettingsWeatherViewModelOutputs {

    val inputs: SettingsWeatherViewModelInputs = this
    val outputs: SettingsWeatherViewModelOutputs = this

    override val weatherTemperatureMetric: MutableStateFlow<Boolean> = MutableStateFlow(weatherRepository.weatherTemperatureMetric)
    override val weatherWindspeedMetric: MutableStateFlow<Boolean> = MutableStateFlow(weatherRepository.weatherWindspeedMetric)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Data.temperatureUnits -> {
                weatherRepository.weatherTemperatureMetric = !weatherRepository.weatherTemperatureMetric
                weatherTemperatureMetric.value = weatherRepository.weatherTemperatureMetric
            }
            Settings.Data.windSpeedUnits -> {
                weatherRepository.weatherWindspeedMetric = !weatherRepository.weatherWindspeedMetric
                weatherWindspeedMetric.value = weatherRepository.weatherWindspeedMetric
            }
        }
    }
}