package tmg.flashback.device.usecases

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.WidgetManager

internal class RefreshWidgetUseCaseTest {

    private val mockWidgetManager: WidgetManager = mockk(relaxed = true)

    private lateinit var underTest: RefreshWidgetUseCase

    object Test { }

    private fun initUnderTest() {
        underTest = RefreshWidgetUseCase(
            widgetManager = mockWidgetManager
        )
    }

    @Test
    fun `updating widget calls manager`() {
        val clazz = Test::class.java
        initUnderTest()

        underTest.update(clazz)

        verify {
            mockWidgetManager.updateWidgets(clazz)
        }
    }
}