package tmg.flashback.widgets.ui

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.widgets.repository.WidgetRepository
import tmg.flashback.widgets.usecases.UpdateWidgetsUseCase
import tmg.testutils.livedata.test
import tmg.testutils.livedata.testObserve

internal class UpNextConfigurationViewModelTest {

    private val widgetId = 1

    private val mockWidgetRepository: WidgetRepository = mockk(relaxed = true)
    private val mockUpdateWidgetsUseCase: UpdateWidgetsUseCase = mockk(relaxed = true)

    private lateinit var underTest: UpNextConfigurationViewModel

    private fun initUnderTest() {
        underTest = UpNextConfigurationViewModel(
            widgetRepository = mockWidgetRepository,
            updateWidgetsUseCase = mockUpdateWidgetsUseCase,
        )
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
        val observer = underTest.outputs.showBackground.testObserve()

        initUnderTest()
        underTest.inputs.changeShowBackground(false)

        observer.assertValue(false)
        verify {
            mockWidgetRepository.setShowBackground(widgetId, false)
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