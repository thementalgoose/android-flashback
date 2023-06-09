package tmg.flashback.weekend.repository

import tmg.flashback.prefs.manager.PreferenceManager
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val preferenceManager: PreferenceManager
) {
    /**
     * Weather metrics
     */
    var weatherTemperatureMetric: Boolean
        get() = preferenceManager.getBoolean(keyWeatherTemperatureMetric, true)
        set(value) = preferenceManager.save(keyWeatherTemperatureMetric, value)
    var weatherWindspeedMetric: Boolean
        get() = preferenceManager.getBoolean(keyWeatherWindspeedMetric, false)
        set(value) = preferenceManager.save(keyWeatherWindspeedMetric, value)

    companion object {
        private const val keyWeatherTemperatureMetric: String = "WEATHER_TEMPERATURE_METRIC"
        private const val keyWeatherWindspeedMetric: String = "WEATHER_WINDSPEED_METRIC"
    }
}