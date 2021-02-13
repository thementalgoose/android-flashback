package tmg.flashback.ui.settings.customisation.animation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test
import tmg.flashback.core.utils.Selected
import tmg.flashback.core.utils.StringHolder
import tmg.flashback.core.ui.bottomsheet.BottomSheetItem

internal class AnimationSpeedViewModelTest: BaseTest() {

    private val mockAppearanceController: AppearanceController = mockk(relaxed = true)

    private lateinit var sut: AnimationSpeedViewModel

    private fun initSUT() {
        sut = AnimationSpeedViewModel(mockAppearanceController)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockAppearanceController.animationSpeed } returns AnimationSpeed.MEDIUM
    }

    //region Animation Speed

    @Test
    fun `init loads animation speed list`() {
        initSUT()
        sut.outputs.animationSpeedPreference.test {
            assertValue(AnimationSpeed.values().map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == AnimationSpeed.MEDIUM)
            })
        }
    }


    @Test
    fun `selecting animation speed updates list`() {
        initSUT()
        sut.inputs.selectAnimationSpeed(AnimationSpeed.QUICK)
        sut.outputs.animationSpeedPreference.test {
            assertValue(AnimationSpeed.values().map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == AnimationSpeed.QUICK)
            })
        }
    }

    @Test
    fun `selecting animation speed updates value in controller`() {
        initSUT()
        sut.inputs.selectAnimationSpeed(AnimationSpeed.QUICK)
        verify {
            mockAppearanceController.animationSpeed = AnimationSpeed.QUICK
        }
    }

    @Test
    fun `selecting animation speed sends animation speed updated event`() {
        initSUT()
        sut.inputs.selectAnimationSpeed(AnimationSpeed.QUICK)
        sut.outputs.animationSpeedUpdated.test {
            assertEventFired()
        }
    }

    //endregion
}