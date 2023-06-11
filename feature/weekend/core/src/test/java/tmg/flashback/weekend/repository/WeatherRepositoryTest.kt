package tmg.flashback.weekend.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tmg.flashback.prefs.manager.PreferenceManager

internal class WeatherRepositoryTest {

    private val mockPreferenceManager: PreferenceManager = mockk(relaxed = true)

    private lateinit var sut: WeatherRepository

    private fun initSUT() {
        sut = WeatherRepository(
            preferenceManager = mockPreferenceManager
        )
    }

    //region Weather

    @Test
    fun `weather temperature metrics reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyWeatherTemperatureMetric, true) } returns true
        initSUT()

        assertTrue(sut.weatherTemperatureMetric)
        verify {
            mockPreferenceManager.getBoolean(keyWeatherTemperatureMetric, true)
        }
    }

    @Test
    fun `weather temperature metrics saves value to shared prefs repository`() {
        initSUT()

        sut.weatherTemperatureMetric = true
        verify {
            mockPreferenceManager.save(keyWeatherTemperatureMetric, true)
        }
    }


    @Test
    fun `weather wind speed metrics reads value from preferences repository`() {
        every { mockPreferenceManager.getBoolean(keyWeatherWindspeedMetric, false) } returns true
        initSUT()

        assertTrue(sut.weatherWindspeedMetric)
        verify {
            mockPreferenceManager.getBoolean(keyWeatherWindspeedMetric, false)
        }
    }

    @Test
    fun `weather wind speed metrics saves value to shared prefs repository`() {
        initSUT()

        sut.weatherWindspeedMetric = true
        verify {
            mockPreferenceManager.save(keyWeatherWindspeedMetric, true)
        }
    }

    //endregion

    companion object {

        private const val keyWeatherTemperatureMetric: String = "WEATHER_TEMPERATURE_METRIC"
        private const val keyWeatherWindspeedMetric: String = "WEATHER_WINDSPEED_METRIC"
    }
}