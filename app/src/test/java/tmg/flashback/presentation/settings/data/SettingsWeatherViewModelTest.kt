package tmg.flashback.presentation.settings.data

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.weekend.repository.WeatherRepository
import tmg.testutils.BaseTest

internal class SettingsWeatherViewModelTest: BaseTest() {

    private val mockWeatherRepository: WeatherRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsWeatherViewModel

    private fun initUnderTest() {
        underTest = SettingsWeatherViewModel(
            weatherRepository = mockWeatherRepository
        )
    }

    @Test
    fun `temperature is true when pref is true`() = runTest(testDispatcher) {
        every { mockWeatherRepository.weatherTemperatureMetric } returns true

        initUnderTest()
        underTest.outputs.weatherTemperatureMetric.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `temperature is false when pref is false`() = runTest(testDispatcher) {
        every { mockWeatherRepository.weatherTemperatureMetric } returns false

        initUnderTest()
        underTest.outputs.weatherTemperatureMetric.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show temperature updates pref and updates value`() = runTest(testDispatcher) {
        every { mockWeatherRepository.weatherTemperatureMetric } returns false

        initUnderTest()
        underTest.outputs.weatherTemperatureMetric.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.temperatureUnits(true))

        verify {
            mockWeatherRepository.weatherTemperatureMetric = true
        }
        underTest.outputs.weatherTemperatureMetric.test { awaitItem() }
    }

    @Test
    fun `windspeed metric is true when pref is true`() = runTest(testDispatcher) {
        every { mockWeatherRepository.weatherWindspeedMetric } returns true

        initUnderTest()
        underTest.outputs.weatherWindspeedMetric.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `windspeed metric is false when pref is false`() = runTest(testDispatcher) {
        every { mockWeatherRepository.weatherWindspeedMetric } returns false

        initUnderTest()
        underTest.outputs.weatherWindspeedMetric.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show windspeed metric updates pref and updates value`() = runTest(testDispatcher) {
        every { mockWeatherRepository.weatherWindspeedMetric } returns false

        initUnderTest()
        underTest.outputs.weatherWindspeedMetric.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.windSpeedUnits(true))

        verify {
            mockWeatherRepository.weatherWindspeedMetric = true
        }
        underTest.outputs.weatherWindspeedMetric.test { awaitItem() }
    }
}