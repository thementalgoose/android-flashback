package tmg.flashback.common.ui.settings.appearance.nightmode

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.ui.bottomsheet.BottomSheetItem
import tmg.flashback.ui.extensions.icon
import tmg.flashback.ui.extensions.label
import tmg.flashback.ui.model.NightMode
import tmg.flashback.ui.repository.ThemeRepository
import tmg.flashback.ui.usecases.ChangeNightModeUseCase
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertDataEventValue
import tmg.testutils.livedata.test
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class NightModeViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk(relaxed = true)
    private val mockChangeNightModeUseCase: ChangeNightModeUseCase = mockk(relaxed = true)

    private lateinit var underTest: NightMoveViewModel

    private fun initUnderTest() {
        underTest = NightMoveViewModel(
            mockThemeRepository,
            mockChangeNightModeUseCase
        )
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.nightMode } returns NightMode.DEFAULT
    }

    @Test
    fun `init loads nightmode list`() {
        initUnderTest()
        underTest.outputs.themePreferences.test {
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
        initUnderTest()
        underTest.inputs.selectNightMode(NightMode.DAY)
        verify {
            mockChangeNightModeUseCase.setNightMode(NightMode.DAY)
        }
    }

    @Test
    fun `selecting nightmode sends nightmode updated event`() {
        initUnderTest()
        underTest.inputs.selectNightMode(NightMode.DAY)
        underTest.outputs.nightModeUpdated.test {
            assertDataEventValue(Pair(NightMode.DAY, false))
        }
    }

    @Test
    fun `selecting nightmode sends nightmode updated event with same selection`() {
        every { mockThemeRepository.nightMode } returns NightMode.DAY
        initUnderTest()
        underTest.inputs.selectNightMode(NightMode.DAY)
        underTest.outputs.nightModeUpdated.test {
            assertDataEventValue(Pair(NightMode.DAY, true))
        }
    }
}