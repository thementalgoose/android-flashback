package tmg.flashback.presentation.settings.widgets

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.flashback.presentation.settings.Settings
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.testutils.BaseTest

class SettingsWidgetViewModelTest: BaseTest() {

    private val mockWidgetRepository: WidgetRepository = mockk(relaxed = true)

    private lateinit var underTest: SettingsWidgetViewModel

    private fun initUnderTest() {
        underTest = SettingsWidgetViewModel(
            widgetRepository = mockWidgetRepository
        )
    }

    @Test
    fun `temperature is true when pref is true`() = runTest(testDispatcher) {
        every { mockWidgetRepository.showBackground } returns true

        initUnderTest()
        underTest.outputs.showBackground.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `temperature is false when pref is false`() = runTest(testDispatcher) {
        every { mockWidgetRepository.showBackground } returns false

        initUnderTest()
        underTest.outputs.showBackground.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show temperature updates pref and updates value`() = runTest(testDispatcher) {
        every { mockWidgetRepository.showBackground } returns false

        initUnderTest()
        underTest.outputs.showBackground.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.temperatureUnits(true))

        verify {
            mockWidgetRepository.showBackground = true
        }
        underTest.outputs.showBackground.test { awaitItem() }
    }

    @Test
    fun `windspeed metric is true when pref is true`() = runTest(testDispatcher) {
        every { mockWidgetRepository.deeplinkToEvent } returns true

        initUnderTest()
        underTest.outputs.deeplinkToEvent.test {
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `windspeed metric is false when pref is false`() = runTest(testDispatcher) {
        every { mockWidgetRepository.deeplinkToEvent } returns false

        initUnderTest()
        underTest.outputs.deeplinkToEvent.test {
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `click show windspeed metric updates pref and updates value`() = runTest(testDispatcher) {
        every { mockWidgetRepository.deeplinkToEvent } returns false

        initUnderTest()
        underTest.outputs.deeplinkToEvent.test { awaitItem() }
        underTest.inputs.prefClicked(Settings.Data.windSpeedUnits(true))

        verify {
            mockWidgetRepository.deeplinkToEvent = true
        }
        underTest.outputs.deeplinkToEvent.test { awaitItem() }
    }
}