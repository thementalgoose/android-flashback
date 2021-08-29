package tmg.common.ui.settings.appearance

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.common.testutils.assertExpectedOrder
import tmg.common.testutils.findPref
import tmg.core.device.managers.BuildConfigManager
import tmg.core.ui.R
import tmg.core.ui.controllers.ThemeController
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAppearanceViewModelTest: BaseTest() {

    private val mockThemeController: ThemeController = mockk()
    private val mockBuildConfigManager: BuildConfigManager = mockk()

    private lateinit var sut: SettingsAppearanceViewModel

    private fun initSUT() {
        sut = SettingsAppearanceViewModel(mockThemeController, mockBuildConfigManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeController.enableThemePicker } returns true
        every { mockBuildConfigManager.isMonetThemeSupported } returns true
    }

    @Test
    fun `initial model list is expected`() {
        every { mockThemeController.enableThemePicker } returns true
        every { mockBuildConfigManager.isMonetThemeSupported } returns true

        initSUT()
        val expected = listOf(
            Pair(R.string.settings_theme_title, null),
            Pair(R.string.settings_theme_theme_title, R.string.settings_theme_theme_description),
            Pair(R.string.settings_theme_nightmode_title, R.string.settings_theme_nightmode_description),
            Pair(R.string.settings_theme_animation_speed_title, R.string.settings_theme_animation_speed_description),
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `initial model list is expected with enable theme picker off`() {
        every { mockThemeController.enableThemePicker } returns false

        initSUT()
        val expected = listOf(
            Pair(R.string.settings_theme_title, null),
            Pair(R.string.settings_theme_nightmode_title, R.string.settings_theme_nightmode_description),
            Pair(R.string.settings_theme_animation_speed_title, R.string.settings_theme_animation_speed_description),
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `initial model list is expected with monet theme not supported`() {
        every { mockBuildConfigManager.isMonetThemeSupported } returns false

        initSUT()
        val expected = listOf(
            Pair(R.string.settings_theme_title, null),
            Pair(R.string.settings_theme_nightmode_title, R.string.settings_theme_nightmode_description),
            Pair(R.string.settings_theme_animation_speed_title, R.string.settings_theme_animation_speed_description),
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref model for night mode shows event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_theme_nightmode_title))
        sut.outputs.openNightMode.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for animation speed shows event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_theme_animation_speed_title))
        sut.outputs.openAnimationSpeed.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for theme shows event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_theme_theme_title))
        sut.outputs.openTheme.test {
            assertEventFired()
        }
    }
}