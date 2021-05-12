package tmg.common.ui.settings.appearance.animation

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
import tmg.core.ui.model.AnimationSpeed
import tmg.utilities.models.Selected
import tmg.utilities.models.StringHolder

internal class AnimationSpeedViewModelTest {

    private val mockAppearanceController: ThemeController = mockk(relaxed = true)

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
                Selected(
                    BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)),
                    it == AnimationSpeed.MEDIUM
                )
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