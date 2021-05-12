package tmg.common.ui.settings.appearance.theme

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.core.ui.controllers.ThemeController
import tmg.core.ui.extensions.icon
import tmg.core.ui.extensions.label
import tmg.core.ui.model.Theme
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class ThemeViewModelTest {

    private val mockThemeController: ThemeController = mockk(relaxed = true)

    private lateinit var sut: ThemeViewModel

    private fun initSUT() {
        sut = ThemeViewModel(mockThemeController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeController.theme } returns Theme.DEFAULT
    }

    @Test
    fun `init loads theme list`() {
        initSUT()
        sut.outputs.themePreferences.test {
            assertValue(Theme.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it == Theme.DEFAULT
                )
            })
        }
    }

    @Test
    fun `selecting theme updates value in controller`() {
        initSUT()
        sut.inputs.selectTheme(Theme.DAY)
        verify {
            mockThemeController.theme = Theme.DAY
        }
    }

    @Test
    fun `selecting theme sends theme updated event`() {
        initSUT()
        sut.inputs.selectTheme(Theme.DAY)
        sut.outputs.themeUpdated.test {
            assertDataEventValue(Pair(Theme.DAY, false))
        }
    }

    @Test
    fun `selecting theme sends theme updated event with same selection`() {
        every { mockThemeController.theme } returns Theme.DAY
        initSUT()
        sut.inputs.selectTheme(Theme.DAY)
        sut.outputs.themeUpdated.test {
            assertDataEventValue(Pair(Theme.DAY, true))
        }
    }
}