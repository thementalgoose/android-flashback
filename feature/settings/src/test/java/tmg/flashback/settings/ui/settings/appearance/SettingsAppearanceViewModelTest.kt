package tmg.flashback.settings.ui.settings.appearance

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tmg.flashback.device.managers.BuildConfigManager
import tmg.flashback.settings.SettingsNavigationComponent
import tmg.flashback.settings.testutils.assertExpectedOrder
import tmg.flashback.settings.testutils.findPref
import tmg.flashback.ui.R
import tmg.flashback.ui.repository.ThemeRepository
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAppearanceViewModelTest: BaseTest() {

    private val mockThemeRepository: ThemeRepository = mockk()
    private val mockBuildConfigManager: BuildConfigManager = mockk()
    private val mockSettingsNavigationComponent: SettingsNavigationComponent = mockk()

    private lateinit var sut: SettingsAppearanceViewModel

    private fun initSUT() {
        sut = SettingsAppearanceViewModel(mockThemeRepository, mockBuildConfigManager, mockSettingsNavigationComponent)
    }

    @BeforeEach
    internal fun setUp() {
        every { mockThemeRepository.enableThemePicker } returns true
        every { mockBuildConfigManager.isMonetThemeSupported } returns true
        every { mockSettingsNavigationComponent.themeDialog() } returns Unit
        every { mockSettingsNavigationComponent.nightModeDialog() } returns Unit
        every { mockSettingsNavigationComponent.animationDialog() } returns Unit
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
        verify {
            mockSettingsNavigationComponent.nightModeDialog()
        }
    }

    @Test
    fun `clicking pref model for animation speed shows event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_theme_animation_speed_title))
        verify {
            mockSettingsNavigationComponent.animationDialog()
        }
    }

    @Test
    fun `clicking pref model for theme shows event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_theme_theme_title))
        verify {
            mockSettingsNavigationComponent.themeDialog()
        }
    }
}