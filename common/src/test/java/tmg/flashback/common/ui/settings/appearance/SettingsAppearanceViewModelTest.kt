package tmg.flashback.common.ui.settings.appearance

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.common.testutils.assertExpectedOrder
import tmg.flashback.common.testutils.findPref
import tmg.flashback.ui.R
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAppearanceViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk()
    private val mockBuildConfigManager: BuildConfigManager = mockk()

    private lateinit var sut: SettingsAppearanceViewModel

    private fun initSUT() {
        sut = SettingsAppearanceViewModel(mockThemeRepository, mockBuildConfigManager)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.enableThemePicker } returns true
        every { mockBuildConfigManager.isMonetThemeSupported } returns true
    }

    @Test
    fun `initial model list is expected`() {
        every { mockThemeRepository.enableThemePicker } returns true
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
        every { mockThemeRepository.enableThemePicker } returns false

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