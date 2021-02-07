package tmg.flashback.ui.settings.customisation

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.components.prefs.AppPreferencesItem
import tmg.flashback.R
import tmg.flashback.core.controllers.AppearanceController
import tmg.flashback.core.enums.AnimationSpeed
import tmg.flashback.core.enums.Theme
import tmg.flashback.extensions.icon
import tmg.flashback.extensions.label
import tmg.flashback.testutils.BaseTest
import tmg.flashback.testutils.assertEventFired
import tmg.flashback.testutils.test
import tmg.flashback.ui.utils.Selected
import tmg.flashback.ui.utils.StringHolder
import tmg.flashback.ui.utils.bottomsheet.BottomSheetItem

internal class SettingsCustomisationViewModelTest: BaseTest() {

    private val mockAppearanceController: AppearanceController = mockk(relaxed = true)

    private lateinit var sut: SettingsCustomisationViewModel

    @BeforeEach
    internal fun setUp() {
        every { mockAppearanceController.animationSpeed } returns AnimationSpeed.MEDIUM
        every { mockAppearanceController.currentTheme } returns Theme.AUTO
    }

    private fun initSUT() {
        sut = SettingsCustomisationViewModel(mockAppearanceController)
    }

    @Test
    fun `init loads correct settings options`() {
        initSUT()
        sut.outputs.settings.test {
            assertValue(listOf(
                AppPreferencesItem.Category(R.string.settings_customisation),
                AppPreferencesItem.Preference("Theme", R.string.settings_theme_theme_title, R.string.settings_theme_theme_description),
                AppPreferencesItem.Preference("AnimationSpeed", R.string.settings_animation_speed_animation_title, R.string.settings_animation_speed_animation_description)
            ))
        }
    }

    //region Theme

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
    fun `clicking theme in settings calls open theme event`() {
        initSUT()
        sut.inputs.preferenceClicked("Theme", null)
        sut.outputs.themeOpenPicker.test {
            assertEventFired()
        }
    }

    @Test
    fun `selecting theme updates list`() {
        initSUT()
        sut.inputs.selectTheme(Theme.DAY)
        sut.outputs.themePreferences.test {
            assertValue(Theme.values().map {
                Selected(BottomSheetItem(it.ordinal, it.icon, StringHolder(it.label)), it == Theme.DAY)
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
            assertEventFired()
        }
    }

    //endregion

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
    fun `clicking animation speed in settings calls open animation speed event`() {
        initSUT()
        sut.inputs.preferenceClicked("AnimationSpeed", null)
        sut.outputs.animationSpeedOpenPicker.test {
            assertEventFired()
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