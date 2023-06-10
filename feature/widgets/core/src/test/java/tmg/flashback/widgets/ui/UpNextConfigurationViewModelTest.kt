package tmg.flashback.widgets.ui

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.flashback.widgets.upnext.configure.UpNextConfigurationViewModel
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCaseImpl
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class UpNextConfigurationViewModelTest: BaseTest() {

    private val widgetId = 1

    private val mockWidgetRepository: WidgetRepository = mockk(relaxed = true)
    private val mockUpdateWidgetsUseCase: UpdateWidgetsUseCaseImpl = mockk(relaxed = true)

    private lateinit var underTest: UpNextConfigurationViewModel

    private fun initUnderTest() {
        underTest = UpNextConfigurationViewModel(
            widgetRepository = mockWidgetRepository,
            updateWidgetsUseCase = mockUpdateWidgetsUseCase,
        )
        every { mockWidgetRepository.getShowBackground(widgetId) } returns true
        every { mockWidgetRepository.getShowWeather(widgetId) } returns true
        underTest.inputs.load(widgetId)
    }

    @Test
    fun `loading widget loads show background`() = runTest {
        every { mockWidgetRepository.getShowBackground(widgetId) } returns true

        initUnderTest()
        underTest.outputs.showBackground.test {

            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `changing background updates background for app widget`() = runTest {

        initUnderTest()

        underTest.outputs.showBackground.test {
            assertEquals(true, awaitItem())
            underTest.inputs.changeShowBackground(false)
            assertEquals(false, awaitItem())
        }
        verify {
            mockWidgetRepository.setShowBackground(widgetId, false)
        }
    }

    @Test
    fun `changing weather updates weather for app widget`() = runTest {

        initUnderTest()

        underTest.outputs.showWeather.test {
            assertEquals(true, awaitItem())
            underTest.inputs.changeShowWeather(false)
            assertEquals(false, awaitItem())
        }
        verify {
            mockWidgetRepository.setShowWeather(widgetId, false)
        }
    }

    @Test
    fun `saving calls update widgets and calls save`() {
        initUnderTest()
        underTest.inputs.save()

        verify {
            mockUpdateWidgetsUseCase.update()
        }
    }
}