package tmg.flashback.widgets.ui

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.flashback.widgets.upnext.configure.UpNextConfigurationViewModel
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class UpNextConfigurationViewModelTest: BaseTest() {

    private val widgetId = 1

    private val mockWidgetRepository: WidgetRepository = mockk(relaxed = true)
    private val mockUpdateWidgetsUseCase: UpdateWidgetsUseCase = mockk(relaxed = true)

    private lateinit var underTest: UpNextConfigurationViewModel

    private fun initUnderTest() {
        underTest = UpNextConfigurationViewModel(
            widgetRepository = mockWidgetRepository,
            updateWidgetsUseCase = mockUpdateWidgetsUseCase,
        )
        every { mockWidgetRepository.getShowBackground(widgetId) } returns true
        underTest.inputs.load(widgetId)
    }

    @Test
    fun `loading widget loads show background`() {
        every { mockWidgetRepository.getShowBackground(widgetId) } returns true

        initUnderTest()
        underTest.outputs.showBackground.test {
            assertValue(true)
        }
    }

    @Test
    fun `changing background updates background for app widget`() {

        initUnderTest()
        underTest.inputs.changeShowBackground(false)

        underTest.outputs.showBackground.test {
            assertValue(false)
        }
        verify {
            mockWidgetRepository.setShowBackground(widgetId, false)
        }
    }

    @Test
    fun `changing weather updates weather for app widget`() {

        initUnderTest()
        underTest.inputs.changeShowWeather(false)

        underTest.outputs.showWeather.test {
            assertValue(false)
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
        underTest.outputs.save.test {
            assertEmittedCount(1)
        }
    }
}