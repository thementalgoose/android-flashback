package tmg.flashback.ui.settings.appearance.nightmode

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class NightModeViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)

    private lateinit var underTest: NightModeViewModel

    private fun initUnderTest() {
        underTest = NightModeViewModel(mockThemeRepository, mockChangeNightModeUseCase)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.nightMode } returns NightMode.DEFAULT
    }

    @Test
    fun `initial value is pulled from theme repository`() {
        initUnderTest()

        underTest.outputs.currentlySelected.test {
            assertValue(NightMode.DEFAULT)
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