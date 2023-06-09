package tmg.flashback.ui.settings.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
import tmg.flashback.weekend.repository.WeatherRepository
import javax.inject.Inject

interface SettingsWeatherViewModelInputs {
    fun prefClicked(pref: Setting)
}

interface SettingsWeatherViewModelOutputs {
    val weatherTemperatureMetric: LiveData<Boolean>
    val weatherWindspeedMetric: LiveData<Boolean>
}

@HiltViewModel
class SettingsWeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
): ViewModel(), SettingsWeatherViewModelInputs, SettingsWeatherViewModelOutputs {

    val inputs: SettingsWeatherViewModelInputs = this
    val outputs: SettingsWeatherViewModelOutputs = this

    override val weatherTemperatureMetric: MutableLiveData<Boolean> = MutableLiveData(weatherRepository.weatherTemperatureMetric)
    override val weatherWindspeedMetric: MutableLiveData<Boolean> = MutableLiveData(weatherRepository.weatherWindspeedMetric)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Data.temperatureUnitsKey -> {
                weatherRepository.weatherTemperatureMetric = !weatherRepository.weatherTemperatureMetric
                weatherTemperatureMetric.value = weatherRepository.weatherTemperatureMetric
            }
            Settings.Data.windSpeedUnitsKey -> {
                weatherRepository.weatherWindspeedMetric = !weatherRepository.weatherWindspeedMetric
                weatherWindspeedMetric.value = weatherRepository.weatherWindspeedMetric
            }
        }
    }
}