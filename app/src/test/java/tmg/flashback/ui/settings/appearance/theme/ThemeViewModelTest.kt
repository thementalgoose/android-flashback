package tmg.flashback.ui.settings.appearance.theme

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.R
import tmg.flashback.ui.managers.ToastManager
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest

internal class ThemeViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockToastManager: ToastManager = mockk(relaxed = true)

    private lateinit var underTest: ThemeViewModel

    private fun initUnderTest() {
        underTest = ThemeViewModel(
            themeRepository = mockThemeRepository,
            toastManager = mockToastManager
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.theme } returns Theme.DEFAULT
    }

    @Test
    fun `initial value is pulled from theme repository`() = runTest {
        initUnderTest()

        underTest.outputs.currentlySelected.test {
            assertEquals(Theme.DEFAULT, awaitItem())
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
            mockToastManager.displayToast(R.string.settings_restart_app_required)
            mockThemeRepository.theme = Theme.MATERIAL_YOU
        }
    }
}