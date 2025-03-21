package tmg.flashback.usecases

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import tmg.flashback.device.usecases.RefreshWidgetUseCase
import tmg.flashback.widgets.presentation.UpNextWidgetReceiver

internal class RefreshWidgetsUseCaseTest {

    private val mockRefreshWidgetUseCase: RefreshWidgetUseCase = mockk(relaxed = true)

    private lateinit var underTest: RefreshWidgetsUseCase

    private fun initUnderTest() {
        underTest = RefreshWidgetsUseCase(
            refreshWidgetsUseCase = mockRefreshWidgetUseCase
        )
    }

    @Test
    fun `update calls up next widget receiver`() {
        initUnderTest()

        underTest.update()

        verify {
            mockRefreshWidgetUseCase.update(UpNextWidgetReceiver::class.java)
        }
    }
}