package tmg.flashback.ui.settings.data

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.ui.settings.Settings
import tmg.flashback.weekend.repository.WeatherRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class SettingsWeatherViewModelTest: BaseTest() {

    private val mockWeatherRepository: WeatherRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsWeatherViewModel

    private fun initUnderTest() {
        underTest = SettingsWeatherViewModel(
            weatherRepository = mockWeatherRepository
        )
    }

    @Test
    fun `temperature is true when pref is true`() {
        every { mockWeatherRepository.weatherTemperatureMetric } returns true

        initUnderTest()
        underTest.outputs.weatherTemperatureMetric.test {
            assertValue(true)
        }
    }

    @Test
    fun `temperature is false when pref is false`() {
        every { mockWeatherRepository.weatherTemperatureMetric } returns false

        initUnderTest()
        underTest.outputs.weatherTemperatureMetric.test {
            assertValue(false)
        }
    }

    @Test
    fun `click show temperature updates pref and updates value`() {
        every { mockWeatherRepository.weatherTemperatureMetric } returns false

        initUnderTest()
        val observer = underTest.outputs.weatherTemperatureMetric.testObserve()
        underTest.inputs.prefClicked(Settings.Data.temperatureUnits(true))

        verify {
            mockWeatherRepository.weatherTemperatureMetric = true
        }
        observer.assertEmittedCount(2)
    }

    @Test
    fun `windspeed metric is true when pref is true`() {
        every { mockWeatherRepository.weatherWindspeedMetric } returns true

        initUnderTest()
        underTest.outputs.weatherWindspeedMetric.test {
            assertValue(true)
        }
    }

    @Test
    fun `windspeed metric is false when pref is false`() {
        every { mockWeatherRepository.weatherWindspeedMetric } returns false

        initUnderTest()
        underTest.outputs.weatherWindspeedMetric.test {
            assertValue(false)
        }
    }

    @Test
    fun `click show windspeed metric updates pref and updates value`() {
        every { mockWeatherRepository.weatherWindspeedMetric } returns false

        initUnderTest()
        val observer = underTest.outputs.weatherWindspeedMetric.testObserve()
        underTest.inputs.prefClicked(Settings.Data.windSpeedUnits(true))

        verify {
            mockWeatherRepository.weatherWindspeedMetric = true
        }
        observer.assertEmittedCount(2)
    }
}