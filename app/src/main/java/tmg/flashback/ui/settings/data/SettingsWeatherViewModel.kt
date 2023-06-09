package tmg.flashback.ui.settings.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import tmg.flashback.results.repository.HomeRepository
import tmg.flashback.ui.settings.Setting
import tmg.flashback.ui.settings.Settings
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
    private val homeRepository: HomeRepository
): ViewModel(), SettingsWeatherViewModelInputs, SettingsWeatherViewModelOutputs {

    val inputs: SettingsWeatherViewModelInputs = this
    val outputs: SettingsWeatherViewModelOutputs = this

    override val weatherTemperatureMetric: MutableLiveData<Boolean> = MutableLiveData(homeRepository.weatherTemperatureMetric)
    override val weatherWindspeedMetric: MutableLiveData<Boolean> = MutableLiveData(homeRepository.weatherWindspeedMetric)

    override fun prefClicked(pref: Setting) {
        when (pref.key) {
            Settings.Data.temperatureUnitsKey -> {
                homeRepository.weatherTemperatureMetric = !homeRepository.weatherTemperatureMetric
                weatherTemperatureMetric.value = homeRepository.weatherTemperatureMetric
            }
            Settings.Data.windSpeedUnitsKey -> {
                homeRepository.weatherWindspeedMetric = !homeRepository.weatherWindspeedMetric
                weatherWindspeedMetric.value = homeRepository.weatherWindspeedMetric
            }
        }
    }
}