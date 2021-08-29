package tmg.common.ui.settings.appearance.nightmode

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.core.ui.bottomsheet.BottomSheetItem
import tmg.core.ui.controllers.ThemeController
import tmg.core.ui.extensions.icon
import tmg.core.ui.extensions.label
import tmg.core.ui.model.NightMode
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class NightModeViewModelTest: BaseTest() {

    private val mockThemeController: ThemeController = mockk(relaxed = true)

    private lateinit var sut: NightMoveViewModel

    private fun initSUT() {
        sut = NightMoveViewModel(mockThemeController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeController.nightMode } returns NightMode.DEFAULT
    }

    @Test
    fun `init loads nightmode list`() {
        initSUT()
        sut.outputs.themePreferences.test {
            assertValue(NightMode.values().map {
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it == NightMode.DEFAULT
                )
            })
        }
    }

    @Test
    fun `selecting nightmode updates value in controller`() {
        initSUT()
        sut.inputs.selectNightMode(NightMode.DAY)
        verify {
            mockThemeController.nightMode = NightMode.DAY
        }
    }

    @Test
    fun `selecting nightmode sends nightmode updated event`() {
        initSUT()
        sut.inputs.selectNightMode(NightMode.DAY)
        sut.outputs.nightModeUpdated.test {
            assertDataEventValue(Pair(NightMode.DAY, false))
        }
    }

    @Test
    fun `selecting nightmode sends nightmode updated event with same selection`() {
        every { mockThemeController.nightMode } returns NightMode.DAY
        initSUT()
        sut.inputs.selectNightMode(NightMode.DAY)
        sut.outputs.nightModeUpdated.test {
            assertDataEventValue(Pair(NightMode.DAY, true))
        }
    }
}