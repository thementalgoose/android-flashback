package tmg.flashback.settings.ui.settings.appearance.theme

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.extensions.icon
import tmg.flashback.ui.extensions.label
import tmg.flashback.ui.model.Theme
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class ThemeViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)

    private lateinit var sut: ThemeViewModel

    private fun initSUT() {
        sut = ThemeViewModel(mockThemeRepository)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.theme } returns Theme.DEFAULT
    }

    @Test
    fun `init loads nightmode list`() {
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
    fun `selecting nightmode updates value in controller`() {
        initSUT()
        sut.inputs.selectTheme(Theme.MATERIAL_YOU)
        verify {
            mockThemeRepository.theme = Theme.MATERIAL_YOU
        }
    }

    @Test
    fun `selecting nightmode sends nightmode updated event`() {
        initSUT()
        sut.inputs.selectTheme(Theme.MATERIAL_YOU)
        sut.outputs.themeUpdated.test {
            assertDataEventValue(Pair(Theme.MATERIAL_YOU, false))
        }
    }

    @Test
    fun `selecting nightmode sends nightmode updated event with same selection`() {
        every { mockThemeRepository.theme } returns Theme.MATERIAL_YOU
        initSUT()
        sut.inputs.selectTheme(Theme.MATERIAL_YOU)
        sut.outputs.themeUpdated.test {
            assertDataEventValue(Pair(Theme.MATERIAL_YOU, true))
        }
    }
}