package tmg.flashback.ui.settings.appearance.nightmode

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.testutils.BaseTest

internal class SettingsNightModeViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)

    private lateinit var underTest: SettingsNightModeViewModel

    private fun initUnderTest() {
        underTest = SettingsNightModeViewModel(mockThemeRepository, mockChangeNightModeUseCase)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.nightMode } returns NightMode.DEFAULT
    }

    @Test
    fun `initial value is pulled from theme repository`() = runTest {
        initUnderTest()

        underTest.outputs.currentlySelected.test {
            assertEquals(NightMode.DEFAULT, awaitItem())
        }
        verify {
            mockThemeRepository.nightMode
        }
    }

    @Test
    fun `selecting theme updates repository`() {
        initUnderTest()
        underTest.inputs.selectNightMode(NightMode.NIGHT)
        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.NIGHT)
        }
    }
}