package tmg.common.ui.settings.appearance

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tmg.core.ui.settings.SettingsModel
import tmg.core.ui.R
import tmg.testutils.livedata.assertEventFired
import tmg.testutils.livedata.test

internal class SettingsAppearanceViewModelTest {

    private lateinit var sut: SettingsAppearanceViewModel

    private fun initSUT() {
        sut = SettingsAppearanceViewModel()
    }

    @Test
    fun `initial model list is expected`() {
        initSUT()
        (sut.models[0] as SettingsModel.Header).apply {
            assertEquals(R.string.settings_theme_title, this.title)
        }
        (sut.models[1] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_theme_theme_title, this.title)
            assertEquals(R.string.settings_theme_theme_description, this.description)
        }
        (sut.models[2] as SettingsModel.Pref).apply {
            assertEquals(R.string.settings_theme_animation_speed_title, this.title)
            assertEquals(R.string.settings_theme_animation_speed_description, this.description)
        }
    }

    @Test
    fun `clicking pref model for theme shows event`() {
        initSUT()
        sut.clickPreference(sut.models[1] as SettingsModel.Pref)
        sut.outputs.openTheme.test {
            assertEventFired()
        }
    }

    @Test
    fun `clicking pref model for animation speed shows event`() {
        initSUT()
        sut.clickPreference(sut.models[2] as SettingsModel.Pref)
        sut.outputs.openAnimationSpeed.test {
            assertEventFired()
        }
    }
}