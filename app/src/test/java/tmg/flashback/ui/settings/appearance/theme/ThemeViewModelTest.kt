package tmg.flashback.ui.settings.appearance.theme

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.test

internal class ThemeViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)

    private lateinit var underTest: ThemeViewModel

    private fun initUnderTest() {
        underTest = ThemeViewModel(mockThemeRepository)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.theme } returns Theme.DEFAULT
    }

    @Test
    fun `initial value is pulled from theme repository`() {
        initUnderTest()

        underTest.outputs.currentlySelected.test {
            assertValue(Theme.DEFAULT)
        }
        verify {
            mockThemeRepository.theme
        }
    }

    @Test
    fun `selecting theme updates repository`() {
        initUnderTest()
        underTest.inputs.selectTheme(Theme.MATERIAL_YOU)
        verify {
            mockThemeRepository.theme = Theme.MATERIAL_YOU
        }
    }
}