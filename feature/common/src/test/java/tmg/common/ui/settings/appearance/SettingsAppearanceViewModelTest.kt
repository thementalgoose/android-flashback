package tmg.common.ui.settings.appearance

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.common.testutils.assertExpectedOrder
import tmg.common.testutils.findPref
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.R
import tmg.testutils.BaseTest
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAppearanceViewModelTest: BaseTest() {

    private lateinit var sut: SettingsAppearanceViewModel

    private fun initSUT() {
        sut = SettingsAppearanceViewModel()
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()
        val expected = listOf(
                Pair(R.string.settings_theme_title, null),
                Pair(R.string.settings_theme_theme_title, R.string.settings_theme_theme_description),
                Pair(R.string.settings_theme_animation_speed_title, R.string.settings_theme_animation_speed_description),
        )

        sut.models.assertExpectedOrder(expected)
    }

    @Test
    fun `clicking pref model for theme shows event`() {
        initSUT()
        sut.clickPreference(sut.models.findPref(R.string.settings_theme_theme_title))
        sut.outputs.openTheme.test {
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
}