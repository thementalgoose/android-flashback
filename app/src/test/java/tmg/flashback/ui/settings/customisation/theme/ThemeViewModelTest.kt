package tmg.flashback.ui.settings.customisation.theme

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.enums.Theme
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test
import tmg.flashback.core.utils.Selected
import tmg.flashback.core.utils.StringHolder
import tmg.flashback.core.ui.bottomsheet.BottomSheetItem
import tmg.flashback.testutils.assertDataEventValue

internal class ThemeViewModelTest: BaseTest() {

    private val mockAppearanceController: AppearanceController = mockk(relaxed = true)

    private lateinit var sut: ThemeViewModel

    private fun initSUT() {
        sut = ThemeViewModel(mockAppearanceController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAppearanceController.currentTheme } returns Theme.AUTO
    }

    @Test
    fun `init loads theme list`() {
        initSUT()
        sut.outputs.themePreferences.test {
            assertValue(Theme.values().map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == Theme.AUTO)
            })
        }
    }

    @Test
    fun `selecting theme updates value in controller`() {
        initSUT()
        sut.inputs.selectTheme(Theme.DAY)
        verify {
            mockAppearanceController.currentTheme = Theme.DAY
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
        every { mockAppearanceController.currentTheme } returns Theme.DAY
        initSUT()
        sut.inputs.selectTheme(Theme.DAY)
        sut.outputs.themeUpdated.test {
            assertDataEventValue(Pair(Theme.DAY, true))
        }
    }
}